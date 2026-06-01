package thong.kotlin.pomodoro.features.pomodoro.task

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import thong.kotlin.pomodoro.core.designsystem.components.AuraButton
import thong.kotlin.pomodoro.core.designsystem.components.AuraInputField
import thong.kotlin.pomodoro.features.pomodoro.timer.presentation.components.BreakEndBannerSmall

@Composable
fun TaskSection(
    tasks: List<Task>,
    newTaskText: String,
    onAddTask: () -> Unit,
    onDeleteTask: (String) -> Unit,
    onToggleTask: (String) -> Unit,
    onNewTaskTextChange: (String) -> Unit,
    showBreakEndBanner: Boolean = false,
    useLazyColumn: Boolean = true,
    modifier: Modifier = Modifier
) {
    val focusManager = LocalFocusManager.current

    Column(modifier = modifier.fillMaxWidth()) {
        AuraInputField(
            value = newTaskText,
            onValueChange = onNewTaskTextChange,
            placeholder = "Hôm nay bạn cần làm gì?",
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                AuraButton(
                    onClick = {
                        onAddTask()
                        focusManager.clearFocus()
                    }
                ) {
                    Text("+", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        if (showBreakEndBanner) {
            BreakEndBannerSmall()
            Spacer(modifier = Modifier.height(12.dp))
        }

        if (useLazyColumn) {
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(tasks, key = { it.id }) { task ->
                    TaskItem(
                        task = task,
                        onToggle = { onToggleTask(task.id) },
                        onDelete = { onDeleteTask(task.id) }
                    )
                }
            }
        } else {
            Column(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                tasks.forEach { task ->
                    TaskItem(
                        task = task,
                        onToggle = { onToggleTask(task.id) },
                        onDelete = { onDeleteTask(task.id) }
                    )
                }
            }
        }
    }
}