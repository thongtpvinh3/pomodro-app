package thong.kotlin.pomodoro.features.test

import thong.kotlin.pomodoro.features.pomodoro.timer.domain.PomodoroMode

data class TestUiState(
    val currentMode: PomodoroMode = PomodoroMode.WORK,
    val timeLeft: Long = 0L,
    val isActive: Boolean = false,
    val pomodorosToday: Int = 0
)