package thong.kotlin.pomodoro.core.database.entity

data class SessionEntity(
    val id: String,
    val modeName: String, // "WORK", "SHORT_BREAK"
    val duration: Int,    // Thời lượng thực tế (phòng khi user bấm Skip giữa chừng)
    val completedAt: Long
)
