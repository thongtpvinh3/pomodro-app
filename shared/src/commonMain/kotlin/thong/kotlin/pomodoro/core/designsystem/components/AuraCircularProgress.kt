package thong.kotlin.pomodoro.core.designsystem.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

@Composable
fun AuraCircularProgress(
    progress: Float,                // Giá trị từ 0.0f đến 1.0f
    progressBrush: Brush,           // Dải màu gradient (ví dụ: AuraGradients.WorkFlow)
    modifier: Modifier = Modifier,
    strokeWidth: Dp = 6.dp
) {
    // Mượt hóa chuyển động của thanh tiến độ khi thời gian giảm dần
    val animatedProgress = animateFloatAsState(
        targetValue = progress,
        animationSpec = tween(durationMillis = 1000)
    )

    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val strokeWidthPx = strokeWidth.toPx()

            // 1. Vẽ vòng tròn nền (Track) phía sau - màu trắng mờ ảo cực nhẹ
            drawCircle(
                color = Color.White.copy(alpha = 0.05f),
                style = Stroke(width = strokeWidthPx)
            )

            // 2. Vẽ vòng tiến trình (Arc) chạy động phía trước
            drawArc(
                brush = progressBrush,
                startAngle = -90f, // Bắt đầu chạy từ đỉnh 12 giờ
                sweepAngle = 360f * animatedProgress.value, // Quay theo phần trăm tiến độ
                useCenter = false,
                style = Stroke(width = strokeWidthPx, cap = StrokeCap.Round) // Bo tròn 2 đầu thanh
            )
        }
    }
}