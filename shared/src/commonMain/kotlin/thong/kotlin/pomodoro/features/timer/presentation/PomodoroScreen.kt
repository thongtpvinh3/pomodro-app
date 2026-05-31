package thong.kotlin.pomodoro.features.timer.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import thong.kotlin.pomodoro.core.designsystem.theme.AuraColors
import thong.kotlin.pomodoro.core.designsystem.theme.AuraGradients
import thong.kotlin.pomodoro.core.designsystem.components.AuraBackground
import thong.kotlin.pomodoro.core.designsystem.components.AuraButton
import thong.kotlin.pomodoro.core.designsystem.components.AuraCheckbox
import thong.kotlin.pomodoro.core.designsystem.components.AuraCircularProgress
import thong.kotlin.pomodoro.core.designsystem.components.AuraHeader
import thong.kotlin.pomodoro.core.designsystem.components.AuraInputField
import thong.kotlin.pomodoro.core.designsystem.components.GlassBox
import thong.kotlin.pomodoro.core.utils.formatToMmSs
import thong.kotlin.pomodoro.features.timer.domain.PomodoroMode

@Composable
fun PomodoroScreen(viewModel: PomodoroViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    var newTaskText by remember { mutableStateOf("") }

    // Giả lập danh sách task để hiển thị cùng component
    val tasks = remember { mutableStateListOf("Hoàn thành UI lõi", "Đọc sách 15 phút") }

    // Quản lý trạng thái Checkbox của các task (Lưu theo String để demo)
    val checkedTasks = remember { mutableStateMapOf<String, Boolean>() }

    // Tự động mượt hóa màu sắc chủ đạo khi đổi chế độ học/nghỉ
    val animatedThemeColor by animateColorAsState(
        targetValue = when (uiState.currentMode) {
            PomodoroMode.WORK -> AuraColors.WorkMode
            PomodoroMode.SHORT_BREAK -> AuraColors.ShortBreakMode
            PomodoroMode.LONG_BREAK -> AuraColors.LongBreakMode
        },
        animationSpec = tween(durationMillis = 500),
        label = "ThemeColorTransition"
    )

    // 2. Tính toán màu nền động cho đồng hồ kính mờ
    val timerBackgroundColor = when (uiState.currentMode) {
        PomodoroMode.WORK -> AuraColors.WorkMode.copy(alpha = 0.15f)
        PomodoroMode.SHORT_BREAK -> AuraColors.ShortBreakMode.copy(alpha = 0.05f)
        PomodoroMode.LONG_BREAK -> AuraColors.LongBreakMode.copy(alpha = 0.05f)
    }

    // 3. Cờ kiểm tra: Vừa hết giờ nghỉ và chuyển sang Học nhưng chưa bấm Bắt đầu
    val isJustEndedBreak = uiState.currentMode == PomodoroMode.WORK &&
            uiState.timeLeft == PomodoroMode.WORK.totalSeconds &&
            !uiState.isActive

    AuraBackground(blurRadius = 3f, overlayAlpha = 0.4f) {
        BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
            // Định nghĩa mốc (Breakpoint): Nếu chiều rộng > 600dp (Tablet hoặc Xoay ngang)
            val isLandscapeOrTablet = maxWidth > 600.dp
            print("+========================== $maxWidth")

            if (isLandscapeOrTablet) {
                // ==========================================
                // GIAO DIỆN TABLET / XOAY NGANG (Chia 2 cột)
                // ==========================================
                Row(
                    modifier = Modifier.fillMaxSize().padding(24.dp),
                    horizontalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    // Cột trái: Đồng hồ
                    Column(
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        TimerSection(uiState, animatedThemeColor, viewModel)
                    }

                    // Cột phải: Quản lý Task
                    Column(
                        modifier = Modifier.weight(1f).fillMaxHeight(),
                    ) {
                        TaskSection(tasks, checkedTasks, newTaskText, { newTaskText = it })
                    }
                }
            } else {
                // ==========================================
                // GIAO DIỆN ĐIỆN THOẠI DỌC (Xếp chồng)
                // ==========================================
                Column(
                    modifier = Modifier.fillMaxSize().padding(20.dp),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    TimerSection(uiState, animatedThemeColor, viewModel)
                    Spacer(modifier = Modifier.height(32.dp))
                    TaskSection(
                        tasks = tasks,
                        checkedTasks = checkedTasks,
                        newTaskText = newTaskText,
                        onTaskTextChange = { newTaskText = it },
                        modifier = Modifier.weight(1f) // Cố định phần dưới cho danh sách cuộn
                    )
                }
            }
        }
    }

    // AuraBackground làm lớp nền mờ ảo cho toàn app
    AuraBackground(blurRadius = 3f, overlayAlpha = 0.4f) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 2. SỬ DỤNG: AuraHeader để làm thanh tiêu đề trên cùng
            AuraHeader(
                title = "Aura Pomo",
                subtitle = "Tìm kiếm dòng chảy học tập",
                actionButton = {
                    IconButton(onClick = { /* Mở cài đặt */ }) {
                        Icon(
                            Icons.Default.Settings,
                            contentDescription = "Settings",
                            tint = Color.White
                        )
                    }
                }
            )

            // 3. SỬ DỤNG: GlassBox nhỏ để hiển thị số Pomo hoàn thành trong ngày
            GlassBox(
                shape = RoundedCornerShape(24.dp),
                modifier = Modifier.padding(vertical = 12.dp)
            ) {
                Text(
                    text = "🔥 ${uiState.pomodorosToday} Pomo hôm nay",
                    color = Color.White,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // 4. SỬ DỤNG: GlassBox hình tròn để làm đồng hồ đếm ngược
            // ==========================
            // KHU VỰC ĐỒNG HỒ TRUNG TÂM
            // ==========================
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier.size(260.dp)
            ) {
                // Vòng tròn tiến trình chạy bám viền
                AuraCircularProgress(
                    progress = (uiState.currentMode.totalSeconds - uiState.timeLeft).toFloat() / uiState.currentMode.totalSeconds,
                    progressBrush = when (uiState.currentMode) {
                        PomodoroMode.WORK -> AuraGradients.WorkFlow
                        else -> AuraGradients.BreakFlow
                    },
                    modifier = Modifier.size(256.dp),
                    strokeWidth = 4.dp
                )

                // Khối kính đồng hồ với màu nền mượt mà
                GlassBox(
                    shape = RoundedCornerShape(120.dp), // Bo tròn tuyệt đối (nửa của 240)
                    backgroundColor = timerBackgroundColor,
                    modifier = Modifier.size(230.dp)
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = uiState.timeLeft.formatToMmSs(),
                            color = AuraColors.TextPrimary,
                            fontSize = 54.sp,
                            fontWeight = FontWeight.Black
                        )
                        Text(
                            text = uiState.currentMode.label.uppercase(),
                            color = animatedThemeColor,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 2.sp
                        )
                    }
                }
            }

            // ==========================================
            // THÔNG BÁO GRADIENT KHI HẾT GIỜ NGHỈ
            // ==========================================
            if (isJustEndedBreak) {
                GlassBox(
                    shape = RoundedCornerShape(16.dp),
                    backgroundBrush = AuraGradients.WorkFlow, // Dải màu năng lượng
                    modifier = Modifier.fillMaxWidth().height(56.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxSize().padding(horizontal = 16.dp),
                        horizontalArrangement = Arrangement.Center,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "⚡ Hết giờ nghỉ! Quay lại tập trung nào",
                            color = Color.White,
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Cụm Nút Điều Khiển
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                AuraButton(onClick = { viewModel.resetTimer() }) {
                    Text("Đặt lại", color = AuraColors.TextSecondary, fontSize = 14.sp)
                }

                Spacer(modifier = Modifier.width(16.dp))

                AuraButton(
                    onClick = { viewModel.toggleTimer() },
                    modifier = Modifier.width(120.dp)
                ) {
                    Text(
                        text = if (uiState.isActive) "Tạm dừng" else "Bắt đầu",
                        color = animatedThemeColor,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }

                Spacer(modifier = Modifier.width(16.dp))

                AuraButton(onClick = { viewModel.skipTimer() }) {
                    Text("Bỏ qua", color = AuraColors.TextSecondary, fontSize = 14.sp)
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Ô nhập Task
            AuraInputField(
                value = newTaskText,
                onValueChange = { newTaskText = it },
                placeholder = "Hôm nay bạn cần làm gì?",
                modifier = Modifier.fillMaxWidth(),
                trailingIcon = {
                    AuraButton(
                        onClick = {
                            if (newTaskText.isNotBlank()) {
                                tasks.add(0, newTaskText)
                                checkedTasks[newTaskText] = false
                                newTaskText = ""
                            }
                        }
                    ) {
                        Text("+", color = Color.White, fontWeight = FontWeight.Bold)
                    }
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Danh sách Task tích hợp AuraCheckbox
            LazyColumn(
                modifier = Modifier.fillMaxWidth(),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(tasks) { task ->
                    val isChecked = checkedTasks[task] ?: false

                    GlassBox(
                        shape = RoundedCornerShape(16.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp, vertical = 14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                // Nút Checkbox xịn xò
                                AuraCheckbox(
                                    checked = isChecked,
                                    onCheckedChange = { checkedTasks[task] = it },
                                    activeColor = AuraColors.WorkMode
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Text(
                                    text = task,
                                    // Làm mờ và gạch ngang chữ nếu đã xong
                                    color = if (isChecked) AuraColors.TextSecondary else Color.White,
                                    fontSize = 15.sp,
                                    textDecoration = if (isChecked) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                                )
                            }

                            IconButton(onClick = {
                                tasks.remove(task)
                                checkedTasks.remove(task)
                            }) {
                                Icon(
                                    imageVector = Icons.Default.Delete,
                                    contentDescription = "Delete",
                                    tint = AuraColors.TextSecondary
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun TimerSection(
    uiState: PomodoroUiState,
    themeColor: Color,
    viewModel: PomodoroViewModel,
    modifier: Modifier = Modifier
) {
    val timerBackgroundColor = when (uiState.currentMode) {
        PomodoroMode.WORK -> AuraColors.WorkMode.copy(alpha = 0.15f)
        PomodoroMode.SHORT_BREAK -> AuraColors.ShortBreakMode.copy(alpha = 0.05f)
        PomodoroMode.LONG_BREAK -> AuraColors.LongBreakMode.copy(alpha = 0.05f)
    }

    val isJustEndedBreak = uiState.currentMode == PomodoroMode.WORK &&
            uiState.timeLeft == PomodoroMode.WORK.totalSeconds &&
            !uiState.isActive

    Column(modifier = modifier, horizontalAlignment = Alignment.CenterHorizontally) {
        AuraHeader(
            title = "Aura Pomo",
            subtitle = "Tìm kiếm dòng chảy học tập",
            actionButton = {
                IconButton(onClick = { /* Mở cài đặt */ }) {
                    Icon(
                        Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = Color.White
                    )
                }
            }
        )

        GlassBox(shape = RoundedCornerShape(24.dp), modifier = Modifier.padding(vertical = 12.dp)) {
            Text(
                text = "🔥 ${uiState.pomodorosToday} Pomo hôm nay",
                color = Color.White, fontSize = 14.sp, fontWeight = FontWeight.SemiBold,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        Box(contentAlignment = Alignment.Center, modifier = Modifier.size(260.dp)) {
            AuraCircularProgress(
                progress = (uiState.currentMode.totalSeconds - uiState.timeLeft).toFloat() / uiState.currentMode.totalSeconds,
                progressBrush = if (uiState.currentMode == PomodoroMode.WORK) AuraGradients.WorkFlow else AuraGradients.BreakFlow,
                modifier = Modifier.size(256.dp), strokeWidth = 4.dp
            )
            GlassBox(
                shape = RoundedCornerShape(120.dp),
                backgroundColor = timerBackgroundColor,
                modifier = Modifier.size(230.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text(
                        text = uiState.timeLeft.formatToMmSs(),
                        color = AuraColors.TextPrimary,
                        fontSize = 54.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = uiState.currentMode.label.uppercase(),
                        color = themeColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (isJustEndedBreak) {
            GlassBox(
                shape = RoundedCornerShape(16.dp),
                backgroundBrush = AuraGradients.WorkFlow,
                modifier = Modifier.fillMaxWidth().height(56.dp)
            ) {
                Text(
                    "⚡ Hết giờ nghỉ! Quay lại tập trung nào",
                    color = Color.White,
                    fontWeight = FontWeight.Bold,
                    fontSize = 15.sp
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxWidth()
        ) {
            AuraButton(onClick = { viewModel.resetTimer() }) {
                Text(
                    "Đặt lại",
                    color = AuraColors.TextSecondary,
                    fontSize = 14.sp
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            AuraButton(onClick = { viewModel.toggleTimer() }, modifier = Modifier.width(120.dp)) {
                Text(
                    text = if (uiState.isActive) "Tạm dừng" else "Bắt đầu",
                    color = themeColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            AuraButton(onClick = { viewModel.skipTimer() }) {
                Text(
                    "Bỏ qua",
                    color = AuraColors.TextSecondary,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Composable
private fun TaskSection(
    tasks: MutableList<String>,
    checkedTasks: MutableMap<String, Boolean>,
    newTaskText: String,
    onTaskTextChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        AuraInputField(
            value = newTaskText,
            onValueChange = onTaskTextChange,
            placeholder = "Hôm nay bạn cần làm gì?",
            modifier = Modifier.fillMaxWidth(),
            trailingIcon = {
                AuraButton(onClick = {
                    if (newTaskText.isNotBlank()) {
                        tasks.add(0, newTaskText)
                        checkedTasks[newTaskText] = false
                        onTaskTextChange("")
                    }
                }) {
                    Text("+", color = Color.White, fontWeight = FontWeight.Bold)
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            items(tasks) { task ->
                val isChecked = checkedTasks[task] ?: false
                GlassBox(shape = RoundedCornerShape(16.dp), modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 14.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            AuraCheckbox(
                                checked = isChecked,
                                onCheckedChange = { checkedTasks[task] = it },
                                activeColor = AuraColors.WorkMode
                            )
                            Spacer(modifier = Modifier.width(12.dp))
                            Text(
                                text = task,
                                color = if (isChecked) AuraColors.TextSecondary else Color.White,
                                fontSize = 15.sp,
                                textDecoration = if (isChecked) androidx.compose.ui.text.style.TextDecoration.LineThrough else null
                            )
                        }
                        IconButton(onClick = { tasks.remove(task); checkedTasks.remove(task) }) {
                            Icon(
                                Icons.Default.Delete,
                                contentDescription = "Delete",
                                tint = AuraColors.TextSecondary
                            )
                        }
                    }
                }
            }
        }
    }
}