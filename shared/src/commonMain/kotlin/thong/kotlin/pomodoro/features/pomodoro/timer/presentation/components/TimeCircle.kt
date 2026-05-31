package thong.kotlin.pomodoro.features.pomodoro.timer.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import thong.kotlin.pomodoro.core.designsystem.components.AuraCircularProgress
import thong.kotlin.pomodoro.core.designsystem.components.GlassBox
import thong.kotlin.pomodoro.core.designsystem.theme.AuraColors
import thong.kotlin.pomodoro.core.designsystem.theme.AuraGradients
import thong.kotlin.pomodoro.core.utils.formatToMmSs
import thong.kotlin.pomodoro.features.pomodoro.timer.domain.PomodoroMode
import thong.kotlin.pomodoro.features.pomodoro.timer.domain.TimerSizes
import thong.kotlin.pomodoro.features.pomodoro.timer.state.PomodoroUiState
import thong.kotlin.pomodoro.features.pomodoro.timer.state.totalSeconds

@Composable
fun TimerCircle(
    uiState: PomodoroUiState,
    themeColor: Color,
    timerBackgroundColor: Color,
    sizes: TimerSizes
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(sizes.outerSize)
    ) {
        val totalSeconds = uiState.currentMode.totalSeconds(uiState.config)
        AuraCircularProgress(
            progress = (totalSeconds - uiState.timeLeft).toFloat() / totalSeconds,
            progressBrush = if (uiState.currentMode == PomodoroMode.WORK) {
                AuraGradients.WorkFlow
            } else {
                AuraGradients.BreakFlow
            },
            modifier = Modifier.size(sizes.progressSize),
            strokeWidth = 4.dp
        )

        GlassBox(
            shape = RoundedCornerShape(sizes.cornerRadius),
            backgroundColor = timerBackgroundColor,
            modifier = Modifier.size(sizes.glassSize)
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = uiState.timeLeft.formatToMmSs(),
                    color = AuraColors.TextPrimary,
                    fontSize = sizes.textSize,
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