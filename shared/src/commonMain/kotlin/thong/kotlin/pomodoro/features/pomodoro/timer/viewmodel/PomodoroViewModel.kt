package thong.kotlin.pomodoro.features.pomodoro.timer.viewmodel

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import kotlin.random.Random
import thong.kotlin.pomodoro.features.pomodoro.timer.domain.EventType
import thong.kotlin.pomodoro.features.pomodoro.timer.domain.PomodoroConfig
import thong.kotlin.pomodoro.features.pomodoro.timer.domain.PomodoroMode
import thong.kotlin.pomodoro.features.pomodoro.task.Task
import thong.kotlin.pomodoro.features.pomodoro.timer.state.PomodoroUiState
import thong.kotlin.pomodoro.features.pomodoro.timer.state.totalSeconds

import thong.kotlin.pomodoro.features.pomodoro.music.data.MusicRepository
import thong.kotlin.pomodoro.features.pomodoro.ambient.data.AmbientSoundRepository
import thong.kotlin.pomodoro.features.settings.data.BackgroundRepository

class PomodoroViewModel(
    private val viewModelScope: CoroutineScope,
    private val soundManager: thong.kotlin.pomodoro.core.media.SoundManager? = null,
    initialState: PomodoroUiState = PomodoroUiState()
) {

    private val _uiState = MutableStateFlow(initialState)
    val uiState: StateFlow<PomodoroUiState> = _uiState.asStateFlow()

    init {
        // Khởi tạo danh sách nhạc và hình nền từ repository tập trung
        _uiState.update { state ->
            state.copy(
                availableTracks = MusicRepository.availableTracks,
                // Chỉ set mặc định nếu state được truyền vào chưa có bài nào được chọn
                selectedTrackId = initialState.selectedTrackId ?: MusicRepository.DEFAULT_TRACK_ID,
                availableAmbientSounds = AmbientSoundRepository.availableSounds,
                availableBackgrounds = BackgroundRepository.availableBackgrounds,
                selectedBackgroundId = initialState.selectedBackgroundId ?: BackgroundRepository.DEFAULT_BACKGROUND_ID
            )
        }

        if (initialState.isActive) {
            resumeTimerAfterRestore()
        }
    }

    // Quản lý Job đếm ngược của Coroutines để có thể hủy (Cancel) bất cứ lúc nào
    private var timerJob: Job? = null

    fun updateConfig(config: PomodoroConfig) {
        timerJob?.cancel()

        _uiState.update {
            it.copy(
                config = config,
                isActive = false,
                timeLeft = it.currentMode.totalSeconds(config),
                event = EventType.NOTHING
            )
        }
    }

    /**
     * Nút bấm chính: Chuyển đổi qua lại giữa Bắt đầu (Play) và Tạm dừng (Pause)
     */
    fun toggleTimer() {
        if (_uiState.value.isActive) {
            pauseTimer()
        } else {
            startTimer()
        }
    }

    private fun startTimer() {
        _uiState.update { currentState ->
            currentState.copy(
                isActive = true,
                event = resolveStartEvent(currentState)
            )
        }

        // Hủy job cũ đề phòng trường hợp bị kích hoạt trùng lặp
        timerJob?.cancel()

        // Kích hoạt vòng lặp đếm ngược bất đồng bộ
        timerJob = viewModelScope.launch {
            while (_uiState.value.timeLeft > 0) {
                delay(1000) // Trì hoãn chính xác 1 giây
                _uiState.update { state ->
                    val newTimeLeft = state.timeLeft - 1
                    
                    // Xử lý âm thanh thông báo giây cuối
                    if (newTimeLeft in 1L..4L) {
                        soundManager?.playBeepSound()
                    }
                    
                    state.copy(timeLeft = newTimeLeft)
                }
            }
            // Khi timeLeft về bằng 0, tự động chạy logic hoàn thành
            handleTimerComplete()
        }
    }

    private fun pauseTimer() {
        timerJob?.cancel()
        _uiState.update { currentState ->
            currentState.copy(
                isActive = false,
                event = resolvePauseEvent(currentState)
            )
        }
    }

    /**
     * Nút Đặt lại (Reset): Đưa thời gian về mốc ban đầu của Chế độ hiện tại
     */
    fun resetTimer() {
        pauseTimer()
        _uiState.update {
            it.copy(
                isActive = false,
                timeLeft = it.currentMode.totalSeconds(it.config),
                event = EventType.NOTHING
            )
        }
    }

    /**
     * Nút Bỏ qua (Skip): Ép kết thúc phiên hiện tại ngay lập tức
     */
    fun skipTimer() {
        timerJob?.cancel()
        val currentState = _uiState.value

        if (currentState.currentMode == PomodoroMode.WORK) {
            // Skip khi chưa học xong thì KHÔNG cộng pomodoro
            _uiState.update {
                it.copy(
                    isActive = false,
                    currentMode = PomodoroMode.SHORT_BREAK,
                    timeLeft = PomodoroMode.SHORT_BREAK.totalSeconds(it.config),
                    event = EventType.NOTHING
                )
            }
        } else {
            // Skip khi đang nghỉ thì quay lại phiên WORK mới
            _uiState.update {
                it.copy(
                    isActive = false,
                    currentMode = PomodoroMode.WORK,
                    timeLeft = PomodoroMode.WORK.totalSeconds(it.config),
                    event = EventType.NOTHING
                )
            }
        }
    }

    /**
     * Người dùng tự chọn thủ công Chế độ học ở thanh chọn dưới đáy
     */
    fun switchMode(mode: PomodoroMode) {
        timerJob?.cancel()
        _uiState.update {
            it.copy(
                isActive = false,
                currentMode = mode,
                timeLeft = mode.totalSeconds(it.config),
                event = EventType.NOTHING
            )
        }
    }

    /**
     * Xử lý tự động khi hết giờ: Học xong thì chuyển sang Nghỉ, Nghỉ xong thì quay lại Học
     */
    private fun handleTimerComplete(resetEvent: Boolean = false) {
        soundManager?.playChimeSound()
        pauseTimer()
        val currentState = _uiState.value

        if (currentState.currentMode == PomodoroMode.WORK) {
            // Nếu vừa học xong: Tăng số Pomo trong ngày và chuyển sang Nghỉ ngắn
            _uiState.update {
                it.copy(
                    pomodorosToday = it.pomodorosToday + 1,
                    currentMode = PomodoroMode.SHORT_BREAK,
                    timeLeft = PomodoroMode.SHORT_BREAK.totalSeconds(it.config),
                    event = EventType.WORK_END
                )
            }
        } else {
            // Nếu vừa nghỉ xong: Quay trở lại chế độ Tập trung
            _uiState.update {
                it.copy(
                    currentMode = PomodoroMode.WORK,
                    timeLeft = PomodoroMode.WORK.totalSeconds(it.config),
                    event = EventType.BREAK_END
                )
            }
        }
    }

    fun onNewTaskTextChange(text: String) {
        _uiState.update { it.copy(newTaskText = text) }
    }

    fun addTask() {
        val text = _uiState.value.newTaskText
        if (text.isNotBlank()) {
            _uiState.update {
                val newTask = Task(id = Random.nextInt().toString(), text = text)
                it.copy(
                    tasks = it.tasks + newTask,
                    newTaskText = ""
                )
            }
        }
    }

    fun deleteTask(taskId: String) {
        _uiState.update { state ->
            state.copy(tasks = state.tasks.filter { it.id != taskId })
        }
    }

    fun toggleTask(taskId: String) {
        _uiState.update { state ->
            state.copy(
                tasks = state.tasks.map {
                    if (it.id == taskId) it.copy(isCompleted = !it.isCompleted) else it
                }
            )
        }
    }

    fun toggleMusic() {
        _uiState.update { state ->
            val newIsPlaying = !state.isMusicPlaying
            if (newIsPlaying) {
                state.selectedTrackId?.let { trackId ->
                    soundManager?.playBackgroundMusic(trackId)
                }
            } else {
                soundManager?.pauseBackgroundMusic()
            }
            state.copy(
                isMusicPlaying = newIsPlaying,
                musicPosition = soundManager?.getCurrentPosition() ?: 0L
            ) 
        }
    }

    fun selectTrack(trackId: String) {
        _uiState.update { state ->
            if (state.isMusicPlaying) {
                soundManager?.playBackgroundMusic(trackId)
            }
            state.copy(
                selectedTrackId = trackId,
                musicPosition = 0L // Reset position when changing track
            )
        }
    }

    fun toggleAmbientSound(soundId: String) {
        _uiState.update { state ->
            val isCurrentlyActive = state.activeAmbientSoundIds.contains(soundId)
            val newActiveIds = if (isCurrentlyActive) {
                soundManager?.stopAmbientSound(soundId)
                state.activeAmbientSoundIds - soundId
            } else {
                soundManager?.playAmbientSound(soundId)
                state.activeAmbientSoundIds + soundId
            }
            state.copy(activeAmbientSoundIds = newActiveIds)
        }
    }

    fun selectBackground(backgroundId: String) {
        _uiState.update { it.copy(selectedBackgroundId = backgroundId) }
    }

    fun toggleTasksExpanded() {
        _uiState.update { it.copy(isTasksExpanded = !it.isTasksExpanded) }
    }

    fun toggleSettings() {
        _uiState.update { state ->
            if (!state.isSettingsVisible) {
                // Pre-fill editing values when opening
                state.copy(
                    isSettingsVisible = true,
                    editingWorkMinutes = state.config.workMinutes.toString(),
                    editingBreakMinutes = state.config.shortBreakMinutes.toString(),
                    settingsError = null
                )
            } else {
                state.copy(isSettingsVisible = false)
            }
        }
    }

    fun onWorkMinutesChange(value: String) {
        _uiState.update { it.copy(editingWorkMinutes = value, settingsError = null) }
    }

    fun onBreakMinutesChange(value: String) {
        _uiState.update { it.copy(editingBreakMinutes = value, settingsError = null) }
    }

    fun saveSettings() {
        val workMin = _uiState.value.editingWorkMinutes.toIntOrNull()
        val breakMin = _uiState.value.editingBreakMinutes.toIntOrNull()

        if (workMin == null || breakMin == null) {
            _uiState.update { it.copy(settingsError = "Vui lòng nhập số hợp lệ") }
            return
        }

        if (workMin !in 1..120) {
            _uiState.update { it.copy(settingsError = "Thời gian tập trung: 1 - 120 phút") }
            return
        }

        if (breakMin !in 1..60) {
            _uiState.update { it.copy(settingsError = "Thời gian nghỉ: 1 - 60 phút") }
            return
        }

        val newConfig = _uiState.value.config.copy(
            workMinutes = workMin,
            shortBreakMinutes = breakMin
        )

        updateConfig(newConfig)
        _uiState.update { it.copy(isSettingsVisible = false) }
    }

    fun resetSettingsToDefault() {
        _uiState.update {
            it.copy(
                editingWorkMinutes = "25",
                editingBreakMinutes = "5",
                settingsError = null
            )
        }
    }

    fun updateBackgroundConfig(config: thong.kotlin.pomodoro.features.background.model.BackgroundConfig) {
        _uiState.update { it.copy(backgroundConfig = config) }
    }

    private fun resumeTimerAfterRestore() {
        timerJob?.cancel()

        timerJob = viewModelScope.launch {
            while (_uiState.value.timeLeft > 0 && _uiState.value.isActive) {
                delay(1000)
                _uiState.update { it.copy(timeLeft = it.timeLeft - 1) }
            }

            if (_uiState.value.timeLeft <= 0) {
                handleTimerComplete()
            }
        }
    }

    private fun resolveStartEvent(state: PomodoroUiState): EventType {
        return when (state.event) {
            EventType.NOTHING,
            EventType.CLICK_PAUSE_WORK -> EventType.CLICK_START_WORK
            EventType.CLICK_PAUSE_BREAK -> EventType.CLICK_START_BREAK
            EventType.CLICK_START_WORK,
            EventType.BREAK_END,
            EventType.WORK_END,
            EventType.CLICK_START_BREAK -> state.event
        }
    }

    private fun resolvePauseEvent(state: PomodoroUiState): EventType {
        return when (state.event) {
            EventType.CLICK_START_WORK -> EventType.CLICK_PAUSE_WORK
            EventType.CLICK_START_BREAK -> EventType.CLICK_PAUSE_BREAK
            EventType.NOTHING,
            EventType.CLICK_PAUSE_WORK,
            EventType.BREAK_END,
            EventType.WORK_END,
            EventType.CLICK_PAUSE_BREAK -> state.event
        }
    }
}