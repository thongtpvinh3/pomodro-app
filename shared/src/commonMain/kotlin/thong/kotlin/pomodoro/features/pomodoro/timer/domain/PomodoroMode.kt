package thong.kotlin.pomodoro.features.pomodoro.timer.domain

enum class PomodoroMode(val label: String, val totalSeconds: Long) {
    STARTUP("Chưa làm gì", 0),
    WORK("Tập trung", 25 * 60),        // 25 phút
    SHORT_BREAK("Nghỉ ngắn", 10),  // 5 phút
    LONG_BREAK("Nghỉ dài", 15 * 60)    // 15 phút
}