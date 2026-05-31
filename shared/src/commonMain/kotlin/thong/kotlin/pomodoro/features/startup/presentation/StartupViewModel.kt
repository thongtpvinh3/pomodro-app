package thong.kotlin.pomodoro.features.startup.presentation

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.async
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import thong.kotlin.pomodoro.features.startup.domain.NextDestination
import thong.kotlin.pomodoro.features.startup.domain.StartupRepository

class StartupViewModel(
    private val repository: StartupRepository,
    private val viewModelScope: CoroutineScope
) {
    private val _uiState = MutableStateFlow<StartupUiState>(StartupUiState.Loading)
    val uiState: StateFlow<StartupUiState> = _uiState.asStateFlow()

    init {
        loadAppDataAndNavigate()
    }

    private fun loadAppDataAndNavigate() {
        viewModelScope.launch {
            // 1. Ghi lại thời điểm bắt đầu load để tính toán thời gian ẩn splash
            val startTime = currentTimeMillis()

            // 2. Kích hoạt LOAD DATA SONG SONG (Parallel Execution) bằng `async`
            val historyDeferred = async { repository.loadStudyHistory() }
            val statsDeferred = async { repository.loadStatistics() }
            val isNewUserDeferred = async { repository.isNewUser() }

            // 3. Đợi tất cả các tác vụ hoàn thành và hứng dữ liệu
            val history = historyDeferred.await()
            val stats = statsDeferred.await()
            val isNewUser = isNewUserDeferred.await()

            // Ở đây bạn có thể lưu dữ liệu vừa load vào một Global State/Memory Cache
            // (ví dụ: UserSession) để màn hình Home/Stats sau này lấy ra dùng ngay lập tức.
            // GlobalCache.init(history, stats)

            // 4. Tính toán thời gian đã trôi qua
            val endTime = currentTimeMillis()
            val elapsedTime = endTime - startTime
            val minimumSplashDuration = 3000L // Ép hiện Splash tối thiểu 3 giây

            // Nếu dữ liệu load xong quá nhanh (dưới 3s), app sẽ đợi nốt số giây còn lại
            if (elapsedTime < minimumSplashDuration) {
                delay(minimumSplashDuration - elapsedTime)
            }

            // 5. Quyết định màn hình tiếp theo dựa trên dữ liệu đã load
            val nextDest = if (isNewUser) NextDestination.ONBOARDING else NextDestination.MAIN_APP
            _uiState.value = StartupUiState.Success(nextDest)
        }
    }

    private fun currentTimeMillis() = 1L

}