package thong.kotlin.pomodoro.features.pomodoro.timer.domain

data class PomodoroConfig(
    val workMinutes: Int = 1,
    val shortBreakMinutes: Int = 1,
    val longBreakMinutes: Int = 15
) {
    val workSeconds: Int get() = workMinutes * 60
    val shortBreakSeconds: Int get() = shortBreakMinutes * 60
    val longBreakSeconds: Int get() = longBreakMinutes * 60
}