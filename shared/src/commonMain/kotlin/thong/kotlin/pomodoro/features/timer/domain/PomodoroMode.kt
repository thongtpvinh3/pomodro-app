package thong.kotlin.pomodoro.features.timer.domain

enum class PomodoroMode(val label: String, val totalSeconds: Long) {
    WORK("Tập trung", 25 * 60),        // 25 phút
    SHORT_BREAK("Nghỉ ngắn", 5 * 60),  // 5 phút
    LONG_BREAK("Nghỉ dài", 15 * 60)    // 15 phút
}