package thong.kotlin.pomodoro.features.pomodoro.timer.state

import androidx.compose.runtime.saveable.listSaver
import thong.kotlin.pomodoro.features.pomodoro.timer.domain.EventType
import thong.kotlin.pomodoro.features.pomodoro.timer.domain.PomodoroMode

val PomodoroUiStateSaver = listSaver<PomodoroUiState, Any>(
    save = {
        listOf(
            it.currentMode.name,
            it.timeLeft,
            it.isActive,
            it.pomodorosToday,
            it.event.name
        )
    },
    restore = {
        PomodoroUiState(
            currentMode = PomodoroMode.valueOf(it[0] as String),
            timeLeft = it[1] as Long,
            isActive = it[2] as Boolean,
            pomodorosToday = it[3] as Int,
            event = EventType.valueOf(it[4] as String)
        )
    }
)