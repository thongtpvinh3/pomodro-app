package thong.kotlin.pomodoro.core.database.dao

import kotlinx.coroutines.flow.Flow
import thong.kotlin.pomodoro.core.database.entity.TaskEntity

interface TaskDao {
    // 1. Lấy toàn bộ danh sách task (Sắp xếp mới nhất lên đầu)
    // Trả về Flow để UI tự động "lắng nghe" sự thay đổi
    fun getAllTasks(): Flow<List<TaskEntity>>

    // 2. Thêm một task mới
    suspend fun insertTask(task: TaskEntity)

    // 3. Cập nhật trạng thái (ví dụ: bấm nút tích hoàn thành)
    suspend fun updateTask(task: TaskEntity)

    // 4. Xóa một task
    suspend fun deleteTaskById(taskId: String)
}