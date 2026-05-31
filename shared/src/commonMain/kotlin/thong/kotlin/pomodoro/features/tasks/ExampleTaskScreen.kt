package thong.kotlin.pomodoro.features.tasks

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.*
import thong.kotlin.pomodoro.core.designsystem.components.AuraButton
import thong.kotlin.pomodoro.core.designsystem.components.AuraHeader
import thong.kotlin.pomodoro.core.designsystem.components.AuraInputField

@Composable
fun ExampleTaskScreen() {
    var taskInput by remember { mutableStateOf("") }

    Column(modifier = Modifier.fillMaxSize().padding(16.dp)) {

        // 1. Gọi thanh tiêu đề đồng bộ từ core
        AuraHeader(
            title = "Công việc",
            subtitle = "Hôm nay bạn cần làm gì?"
        )

        // 2. Gọi ô nhập liệu trong suốt từ core
        AuraInputField(
            value = taskInput,
            onValueChange = { taskInput = it },
            placeholder = "Nhập tên công việc...",
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                // Lồng nút bấm co giãn AuraButton vào cạnh phải ô nhập liệu
                AuraButton(onClick = { /* Xử lý thêm task */ }) {
                    Text("+", color = Color.White)
                }
            }
        )
    }
}