package thong.kotlin.pomodoro.features.pomodoro.timer.state

import thong.kotlin.pomodoro.features.pomodoro.timer.domain.EventType
import thong.kotlin.pomodoro.features.pomodoro.timer.domain.PomodoroConfig
import thong.kotlin.pomodoro.features.pomodoro.timer.domain.PomodoroMode
import thong.kotlin.pomodoro.features.pomodoro.task.Task
import thong.kotlin.pomodoro.features.pomodoro.music.domain.MusicTrack

import thong.kotlin.pomodoro.features.settings.domain.AppBackground

data class PomodoroUiState(
    val currentMode: PomodoroMode = PomodoroMode.WORK,
    val config: PomodoroConfig = PomodoroConfig(),
    val timeLeft: Long = currentMode.totalSeconds(config),
    val isActive: Boolean = false,
    val pomodorosToday: Int = 0,
    val event: EventType = EventType.NOTHING,
    val tasks: List<Task> = emptyList(),
    val newTaskText: String = "",
    val availableTracks: List<MusicTrack> = emptyList(),
    val selectedTrackId: String? = null,
    val isMusicPlaying: Boolean = false,
    val musicPosition: Long = 0L,
    val availableBackgrounds: List<AppBackground> = emptyList(),
    val selectedBackgroundId: String? = null,
    val isTasksExpanded: Boolean = false
) {
    val isJustEndedBreak: Boolean
        get() = currentMode == PomodoroMode.WORK &&
                timeLeft == currentMode.totalSeconds(config) &&
                !isActive && event == EventType.BREAK_END
}

fun PomodoroMode.totalSeconds(config: PomodoroConfig): Long {
    return when (this) {
        PomodoroMode.WORK -> config.workSeconds.toLong()
        PomodoroMode.SHORT_BREAK -> config.shortBreakSeconds.toLong()
        PomodoroMode.LONG_BREAK -> config.longBreakSeconds.toLong()
    }
}