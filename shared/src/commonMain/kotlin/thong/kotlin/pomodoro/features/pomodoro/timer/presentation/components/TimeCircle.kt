package thong.kotlin.pomodoro.features.pomodoro.timer.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
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
import thong.kotlin.pomodoro.features.background.model.PerformanceMode
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
    val performanceMode = uiState.backgroundConfig.performanceMode
    
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.size(sizes.outerSize)
    ) {
        val totalSeconds = remember(uiState.currentMode, uiState.config) {
            uiState.currentMode.totalSeconds(uiState.config)
        }
        val progress = remember(totalSeconds, uiState.timeLeft) {
            (totalSeconds - uiState.timeLeft).toFloat() / totalSeconds
        }
        val progressBrush = remember(uiState.currentMode) {
            if (uiState.currentMode == PomodoroMode.WORK) {
                AuraGradients.WorkFlow
            } else {
                AuraGradients.BreakFlow
            }
        }

        AuraCircularProgress(
            progress = progress,
            progressBrush = progressBrush,
            modifier = Modifier.size(sizes.progressSize),
            strokeWidth = 4.dp,
            animate = performanceMode != PerformanceMode.POWER_SAVER
        )

        GlassBox(
            shape = RoundedCornerShape(sizes.cornerRadius),
            backgroundColor = timerBackgroundColor,
            animateColor = performanceMode != PerformanceMode.POWER_SAVER,
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