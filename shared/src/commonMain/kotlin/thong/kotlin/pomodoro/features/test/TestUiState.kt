package thong.kotlin.pomodoro.features.test

import thong.kotlin.pomodoro.features.timer.domain.PomodoroMode

data class TestUiState(
    val currentMode: PomodoroMode = PomodoroMode.WORK,
    val timeLeft: Long = PomodoroMode.WORK.totalSeconds,
    val isActive: Boolean = false,
    val pomodorosToday: Int = 0
)