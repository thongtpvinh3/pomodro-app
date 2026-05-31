package thong.kotlin.pomodoro.features.timer.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import thong.kotlin.pomodoro.features.timer.domain.PomodoroMode

class PomodoroViewModel(private val viewModelScope: CoroutineScope) {

    private val _uiState = MutableStateFlow(PomodoroUiState())
    val uiState: StateFlow<PomodoroUiState> = _uiState.asStateFlow()

    // Quản lý Job đếm ngược của Coroutines để có thể hủy (Cancel) bất cứ lúc nào
    private var timerJob: Job? = null

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
        _uiState.update { it.copy(isActive = true) }

        // Hủy job cũ đề phòng trường hợp bị kích hoạt trùng lặp
        timerJob?.cancel()

        // Kích hoạt vòng lặp đếm ngược bất đồng bộ
        timerJob = viewModelScope.launch {
            while (_uiState.value.timeLeft > 0) {
                delay(1000) // Trì hoãn chính xác 1 giây
                _uiState.update { it.copy(timeLeft = it.timeLeft - 1) }
            }
            // Khi timeLeft về bằng 0, tự động chạy logic hoàn thành
            handleTimerComplete()
        }
    }

    private fun pauseTimer() {
        timerJob?.cancel() // Dừng luồng đếm ngược của Coroutines
        _uiState.update { it.copy(isActive = false) }
    }

    /**
     * Nút Đặt lại (Reset): Đưa thời gian về mốc ban đầu của Chế độ hiện tại
     */
    fun resetTimer() {
        pauseTimer()
        _uiState.update { it.copy(timeLeft = it.currentMode.totalSeconds) }
    }

    /**
     * Nút Bỏ qua (Skip): Ép kết thúc phiên hiện tại ngay lập tức
     */
    fun skipTimer() {
        handleTimerComplete()
    }

    /**
     * Người dùng tự chọn thủ công Chế độ học ở thanh chọn dưới đáy
     */
    fun switchMode(mode: PomodoroMode) {
        pauseTimer()
        _uiState.update {
            it.copy(currentMode = mode, timeLeft = mode.totalSeconds)
        }
    }

    /**
     * Xử lý tự động khi hết giờ: Học xong thì chuyển sang Nghỉ, Nghỉ xong thì quay lại Học
     */
    private fun handleTimerComplete() {
        pauseTimer()
        val currentState = _uiState.value

        if (currentState.currentMode == PomodoroMode.WORK) {
            // Nếu vừa học xong: Tăng số Pomo trong ngày và chuyển sang Nghỉ ngắn
            _uiState.update {
                it.copy(
                    pomodorosToday = it.pomodorosToday + 1,
                    currentMode = PomodoroMode.SHORT_BREAK,
                    timeLeft = PomodoroMode.SHORT_BREAK.totalSeconds
                )
            }
        } else {
            // Nếu vừa nghỉ xong: Quay trở lại chế độ Tập trung
            _uiState.update {
                it.copy(
                    currentMode = PomodoroMode.WORK,
                    timeLeft = PomodoroMode.WORK.totalSeconds
                )
            }
        }
    }
}