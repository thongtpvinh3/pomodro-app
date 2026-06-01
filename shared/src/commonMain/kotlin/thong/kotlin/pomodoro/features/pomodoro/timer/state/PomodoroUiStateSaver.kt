package thong.kotlin.pomodoro.features.pomodoro.timer.state

import androidx.compose.runtime.saveable.listSaver
import thong.kotlin.pomodoro.features.pomodoro.timer.domain.EventType
import thong.kotlin.pomodoro.features.pomodoro.timer.domain.PomodoroMode
import thong.kotlin.pomodoro.features.pomodoro.task.Task

val PomodoroUiStateSaver = listSaver(
    save = {
        listOf(
            it.currentMode.name,
            it.timeLeft,
            it.isActive,
            it.pomodorosToday,
            it.event.name,
            it.newTaskText,
            // Chuyển danh sách Task thành danh sách các thuộc tính đơn giản để có thể lưu trữ
            it.tasks.map { task ->
                listOf(task.id, task.text, task.isCompleted)
            },
            it.selectedTrackId,
            it.isMusicPlaying,
            it.musicPosition,
            it.selectedBackgroundId,
            it.isTasksExpanded
        )
    },
    restore = {
        val tasksRaw = it[6] as List<List<Any>>
        val restoredTasks = tasksRaw.map { rawTask ->
            Task(
                id = rawTask[0] as String,
                text = rawTask[1] as String,
                isCompleted = rawTask[2] as Boolean
            )
        }

        PomodoroUiState(
            currentMode = PomodoroMode.valueOf(it[0] as String),
            timeLeft = it[1] as Long,
            isActive = it[2] as Boolean,
            pomodorosToday = it[3] as Int,
            event = EventType.valueOf(it[4] as String),
            newTaskText = it[5] as String,
            tasks = restoredTasks,
            selectedTrackId = it[7] as String?,
            isMusicPlaying = it[8] as Boolean,
            musicPosition = it[9] as Long,
            selectedBackgroundId = it[10] as String?,
            isTasksExpanded = it[11] as Boolean
        )
    }
)
