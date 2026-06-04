package thong.kotlin.pomodoro.features.pomodoro.task

import androidx.compose.runtime.Immutable

@Immutable
data class Task(
    val id: String,
    val text: String,
    val isCompleted: Boolean = false
)