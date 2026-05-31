package thong.kotlin.pomodoro.features.timer.presentation

import thong.kotlin.pomodoro.features.timer.domain.PomodoroMode

data class PomodoroUiState(
    val currentMode: PomodoroMode = PomodoroMode.WORK,
    val timeLeft: Long = PomodoroMode.WORK.totalSeconds,
    val isActive: Boolean = false,
    val pomodorosToday: Int = 0
)