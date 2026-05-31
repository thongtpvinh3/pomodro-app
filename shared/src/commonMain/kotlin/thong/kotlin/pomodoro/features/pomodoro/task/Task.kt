package thong.kotlin.pomodoro.features.pomodoro.task

data class Task(
    val id: String,
    val text: String,
    val isCompleted: Boolean = false
)