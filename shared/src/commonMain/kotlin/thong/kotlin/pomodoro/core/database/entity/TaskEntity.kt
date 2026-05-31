package thong.kotlin.pomodoro.core.database.entity

data class TaskEntity(
    val id: String,
    val title: String,
    val isCompleted: Boolean,
    val createdAt: Long
)
