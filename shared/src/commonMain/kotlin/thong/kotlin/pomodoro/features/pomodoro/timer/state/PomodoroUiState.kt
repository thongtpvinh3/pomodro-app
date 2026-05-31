package thong.kotlin.pomodoro.features.pomodoro.timer.state

import thong.kotlin.pomodoro.features.pomodoro.timer.domain.EventType
import thong.kotlin.pomodoro.features.pomodoro.timer.domain.PomodoroConfig
import thong.kotlin.pomodoro.features.pomodoro.timer.domain.PomodoroMode
import thong.kotlin.pomodoro.features.pomodoro.task.Task

data class PomodoroUiState(
    val currentMode: PomodoroMode = PomodoroMode.WORK,
    val timeLeft: Long = PomodoroMode.WORK.totalSeconds,
    val isActive: Boolean = false,
    val pomodorosToday: Int = 0,
    val event: EventType = EventType.NOTHING,
    val config: PomodoroConfig = PomodoroConfig(),
    val tasks: List<Task> = emptyList(),
    val newTaskText: String = ""
) {
    val isJustEndedBreak: Boolean
        get() = currentMode == PomodoroMode.WORK &&
                timeLeft == currentMode.totalSeconds(config) &&
                !isActive &&
                event == EventType.BREAK_END
}

fun PomodoroMode.totalSeconds(config: PomodoroConfig): Long {
    return when (this) {
        PomodoroMode.WORK -> config.workSeconds.toLong()
        PomodoroMode.SHORT_BREAK -> config.shortBreakSeconds.toLong()
        PomodoroMode.LONG_BREAK -> config.longBreakSeconds.toLong()
        PomodoroMode.STARTUP -> config.workSeconds.toLong()
    }
}