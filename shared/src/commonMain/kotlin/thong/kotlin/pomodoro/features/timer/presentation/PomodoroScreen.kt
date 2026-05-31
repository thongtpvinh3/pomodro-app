package thong.kotlin.pomodoro.features.timer.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.TextUnit
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import thong.kotlin.pomodoro.core.designsystem.components.AuraBackground
import thong.kotlin.pomodoro.core.designsystem.components.AuraButton
import thong.kotlin.pomodoro.core.designsystem.components.AuraCheckbox
import thong.kotlin.pomodoro.core.designsystem.components.AuraCircularProgress
import thong.kotlin.pomodoro.core.designsystem.components.AuraHeader
import thong.kotlin.pomodoro.core.designsystem.components.AuraInputField
import thong.kotlin.pomodoro.core.designsystem.components.GlassBox
import thong.kotlin.pomodoro.core.designsystem.theme.AuraColors
import thong.kotlin.pomodoro.core.designsystem.theme.AuraGradients
import thong.kotlin.pomodoro.core.utils.formatToMmSs
import thong.kotlin.pomodoro.features.timer.domain.EventType
import thong.kotlin.pomodoro.features.timer.domain.PomodoroMode
import thong.kotlin.pomodoro.features.timer.model.PomodoroUiState
import thong.kotlin.pomodoro.features.timer.viewmodel.PomodoroViewModel

@Composable
fun PomodoroScreenResponsive(viewModel: PomodoroViewModel) {

    val uiState by viewModel.uiState.collectAsState()
    var newTaskText by remember { mutableStateOf("") }
    val tasks = remember {
        mutableStateListOf("Hoàn thành UI lõi", "Đọc sách 15 phút")
    }
    val checkedTasks = remember {
        mutableStateMapOf<String, Boolean>()
    }
    val animatedThemeColor by animateColorAsState(
        targetValue = when (uiState.currentMode) {
            PomodoroMode.STARTUP -> AuraColors.MainAppMode
            PomodoroMode.WORK -> AuraColors.WorkMode
            PomodoroMode.SHORT_BREAK -> AuraColors.ShortBreakMode
            PomodoroMode.LONG_BREAK -> AuraColors.LongBreakMode
        },
        animationSpec = tween(durationMillis = 500),
        label = "ThemeColorTransition"
    )
    AuraBackground(blurRadius = 3f, overlayAlpha = 0.4f) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            val isLandscape = maxWidth > maxHeight

            if (isLandscape) {
                LandscapePomodoroContent(
                    uiState = uiState,
                    themeColor = animatedThemeColor,
                    viewModel = viewModel,
                    tasks = tasks,
                    checkedTasks = checkedTasks,
                    newTaskText = newTaskText,
                    onTaskTextChange = { newTaskText = it }
                )
            } else {
                PortraitPomodoroContent(
                    uiState = uiState,
                    themeColor = animatedThemeColor,
                    viewModel = viewModel,
                    tasks = tasks,
                    checkedTasks = checkedTasks,
                    newTaskText = newTaskText,
                    onTaskTextChange = { newTaskText = it }
                )
            }
        }
    }
}

@Composable
private fun PortraitPomodoroContent(
    uiState: PomodoroUiState,
    themeColor: Color,
    viewModel: PomodoroViewModel,
    tasks: MutableList<String>,
    checkedTasks: MutableMap<String, Boolean>,
    newTaskText: String,
    onTaskTextChange: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        TimerSection(
            uiState = uiState,
            themeColor = themeColor,
            viewModel = viewModel,
            modifier = Modifier.fillMaxWidth()
        )

        Spacer(modifier = Modifier.height(32.dp))

        TaskSection(
            tasks = tasks,
            checkedTasks = checkedTasks,
            newTaskText = newTaskText,
            onTaskTextChange = onTaskTextChange,
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun LandscapePomodoroContent(
    uiState: PomodoroUiState,
    themeColor: Color,
    viewModel: PomodoroViewModel,
    tasks: MutableList<String>,
    checkedTasks: MutableMap<String, Boolean>,
    newTaskText: String,
    onTaskTextChange: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 20.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        TimerSection(
            uiState = uiState,
            themeColor = themeColor,
            viewModel = viewModel,
            compact = true,
            modifier = Modifier.weight(1f)
        )

        TaskSection(
            tasks = tasks,
            checkedTasks = checkedTasks,
            newTaskText = newTaskText,
            onTaskTextChange = onTaskTextChange,
            modifier = Modifier.weight(1f)
        )
    }
}

@Composable
fun TimerSection(
    uiState: PomodoroUiState,
    themeColor: Color,
    viewModel: PomodoroViewModel,
    compact: Boolean = false,
    modifier: Modifier = Modifier
) {
    val timerBackgroundColor = when (uiState.currentMode) {
        PomodoroMode.STARTUP -> AuraColors.MainAppMode.copy(alpha = 0.2f)
        PomodoroMode.WORK -> AuraColors.WorkMode.copy(alpha = 0.15f)
        PomodoroMode.SHORT_BREAK -> AuraColors.ShortBreakMode.copy(alpha = 0.05f)
        PomodoroMode.LONG_BREAK -> AuraColors.LongBreakMode.copy(alpha = 0.05f)
    }

    val isJustEndedBreak = uiState.currentMode == PomodoroMode.WORK &&
            uiState.timeLeft == PomodoroMode.WORK.totalSeconds &&
            !uiState.isActive &&
            uiState.event == EventType.BREAK_END

    val timerOuterSize = if (compact) 190.dp else 260.dp
    val progressSize = if (compact) 206.dp else 256.dp
    val timerGlassSize = if (compact) 184.dp else 230.dp
    val timerCornerRadius = if (compact) 92.dp else 120.dp
    val timerTextSize = if (compact) 42.sp else 54.sp

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = if (compact) Arrangement.Center else Arrangement.Top
    ) {
        AuraHeader(
            title = "Aura Pomo",
            subtitle = "Tìm kiếm dòng chảy học tập",
            actionButton = {
                IconButton(onClick = { /* Mở cài đặt */ }) {
                    Icon(
                        imageVector = Icons.Default.Settings,
                        contentDescription = "Settings",
                        tint = Color.White
                    )
                }
            }
        )

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

        if (compact) {
            // Đồng hồ nằm bên trái, button nằm dọc bên phải đồng hồ.
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                TimerCircle(
                    uiState = uiState,
                    themeColor = themeColor,
                    timerBackgroundColor = timerBackgroundColor,
                    timerOuterSize = timerOuterSize,
                    progressSize = progressSize,
                    timerGlassSize = timerGlassSize,
                    timerCornerRadius = timerCornerRadius,
                    timerTextSize = timerTextSize
                )

                Spacer(modifier = Modifier.width(16.dp))

                TimerControlButtonsVertical(
                    uiState = uiState,
                    themeColor = themeColor,
                    viewModel = viewModel
                )
            }
        } else {
            Spacer(modifier = Modifier.height(16.dp))

            TimerCircle(
                uiState = uiState,
                themeColor = themeColor,
                timerBackgroundColor = timerBackgroundColor,
                timerOuterSize = timerOuterSize,
                progressSize = progressSize,
                timerGlassSize = timerGlassSize,
                timerCornerRadius = timerCornerRadius,
                timerTextSize = timerTextSize
            )

            Spacer(modifier = Modifier.height(24.dp))

            if (isJustEndedBreak) {
                GlassBox(
                    shape = RoundedCornerShape(16.dp),
                    backgroundBrush = AuraGradients.WorkFlow,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(56.dp)
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 16.dp),
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
            TimerControlButtonsHorizontal(
                uiState = uiState,
                themeColor = themeColor,
                viewModel = viewModel
            )
        }
    }
}

@Composable
private fun TimerCircle(
    uiState: PomodoroUiState,
    themeColor: Color,
    timerBackgroundColor: Color,
    timerOuterSize: Dp,
    progressSize: Dp,
    timerGlassSize: Dp,
    timerCornerRadius: Dp,
    timerTextSize: TextUnit
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(timerOuterSize)
    ) {
        AuraCircularProgress(
            progress = (uiState.currentMode.totalSeconds - uiState.timeLeft).toFloat() /
                    uiState.currentMode.totalSeconds,
            progressBrush = if (uiState.currentMode == PomodoroMode.WORK) {
                AuraGradients.WorkFlow
            } else {
                AuraGradients.BreakFlow
            },
            modifier = Modifier.size(progressSize),
            strokeWidth = 4.dp
        )

        GlassBox(
            shape = RoundedCornerShape(timerCornerRadius),
            backgroundColor = timerBackgroundColor,
            modifier = Modifier.size(timerGlassSize)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = uiState.timeLeft.formatToMmSs(),
                    color = AuraColors.TextPrimary,
                    fontSize = timerTextSize,
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
}

@Composable
private fun TimerControlButtonsHorizontal(
    uiState: PomodoroUiState,
    themeColor: Color,
    viewModel: PomodoroViewModel
) {
    BoxWithConstraints(modifier = Modifier.fillMaxWidth()) {
        val buttonGap = 10.dp
        val buttonWidth = (maxWidth - buttonGap * 2) / 3
        val buttonModifier = Modifier
            .width(buttonWidth)
            .height(48.dp)

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(buttonGap),
            modifier = Modifier.fillMaxWidth()
        ) {
            AuraButton(
                onClick = { viewModel.resetTimer() },
                modifier = buttonModifier,
                horizontalPadding = 4.dp,
                verticalPadding = 8.dp,
                fillContent = true
            ) {
                TimerButtonText(
                    text = "Đặt lại",
                    color = AuraColors.TextSecondary,
                    fontSize = 13.sp
                )
            }

            AuraButton(
                onClick = { viewModel.toggleTimer() },
                modifier = buttonModifier,
                horizontalPadding = 4.dp,
                verticalPadding = 8.dp,
                fillContent = true
            ) {
                TimerButtonText(
                    text = if (uiState.isActive) "Tạm dừng" else "Bắt đầu",
                    color = themeColor,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            AuraButton(
                onClick = { viewModel.skipTimer() },
                modifier = buttonModifier,
                horizontalPadding = 4.dp,
                verticalPadding = 8.dp,
                fillContent = true
            ) {
                TimerButtonText(
                    text = "Bỏ qua",
                    color = AuraColors.TextSecondary,
                    fontSize = 13.sp
                )
            }
        }
    }
}

@Composable
private fun TimerControlButtonsVertical(
    uiState: PomodoroUiState,
    themeColor: Color,
    viewModel: PomodoroViewModel
) {
    val buttonModifier = Modifier
        .fillMaxWidth()
        .height(48.dp)

    Column(
        modifier = Modifier.width(132.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        AuraButton(
            onClick = { viewModel.resetTimer() },
            modifier = buttonModifier,
            horizontalPadding = 8.dp,
            verticalPadding = 8.dp
        ) {
            TimerButtonText("Đặt lại", color = AuraColors.TextSecondary, fontSize = 13.sp)
        }

        AuraButton(
            onClick = { viewModel.toggleTimer() },
            modifier = buttonModifier,
            horizontalPadding = 8.dp,
            verticalPadding = 8.dp
        ) {
            TimerButtonText(
                text = if (uiState.isActive) "Tạm dừng" else "Bắt đầu",
                color = themeColor,
                fontWeight = FontWeight.Bold,
                fontSize = 15.sp
            )
        }

        AuraButton(
            onClick = { viewModel.skipTimer() },
            modifier = buttonModifier,
            horizontalPadding = 8.dp,
            verticalPadding = 8.dp
        ) {
            TimerButtonText("Bỏ qua", color = AuraColors.TextSecondary, fontSize = 13.sp)
        }
    }
}

@Composable
private fun TimerButtonText(
    text: String,
    color: Color,
    fontSize: TextUnit,
    fontWeight: FontWeight = FontWeight.SemiBold
) {
    Text(
        text = text,
        color = color,
        fontSize = fontSize,
        fontWeight = fontWeight,
        textAlign = TextAlign.Center,
        maxLines = 1,
        overflow = TextOverflow.Ellipsis,
        modifier = Modifier.fillMaxWidth()
    )
}

@Composable
fun TaskSection(
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
                AuraButton(
                    onClick = {
                        if (newTaskText.isNotBlank()) {
                            tasks.add(0, newTaskText)
                            checkedTasks[newTaskText] = false
                            onTaskTextChange("")
                        }
                    }
                ) {
                    Text(
                        text = "+",
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
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
                                textDecoration = if (isChecked) TextDecoration.LineThrough else null
                            )
                        }

                        IconButton(
                            onClick = {
                                tasks.remove(task)
                                checkedTasks.remove(task)
                            }
                        ) {
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