package thong.kotlin.pomodoro.core.designsystem.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.unit.*
import thong.kotlin.pomodoro.core.designsystem.theme.AuraAnimations
import thong.kotlin.pomodoro.core.designsystem.theme.AuraGradients

@Composable
fun GlassBox(
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    backgroundColor: Color = Color.White.copy(alpha = 0.07f),
    backgroundBrush: Brush? = null,                           // Hỗ trợ nền Gradient nếu cần
    borderBrush: Brush = AuraGradients.GlassBorder,           // TÍCH HỢP: Viền kính mờ từ AuraGradients
    contentAlignment: Alignment = Alignment.Center,
    content: @Composable BoxScope.() -> Unit
) {

    // TÍCH HỢP: AuraAnimations
    // Tự động mượt hóa màu nền bằng thông số SmoothEasing (nhanh ở đầu, mượt ở cuối)
    val animatedBgColor by animateColorAsState(
        targetValue = backgroundColor,
        animationSpec = AuraAnimations.colorTween(),
        label = "GlassBoxBackgroundAnimation"
    )

    // Xử lý linh hoạt Modifier đổ nền (Ưu tiên Gradient nếu có, không có thì dùng màu đơn có hiệu ứng)
    val backgroundModifier = if (backgroundBrush != null) {
        Modifier.background(backgroundBrush)
    } else {
        Modifier.background(animatedBgColor)
    }

    Box(
        modifier = modifier
            .clip(shape)
            .then(backgroundModifier)
            .border(width = 1.dp, brush = borderBrush, shape = shape),
        contentAlignment = contentAlignment,
        content = content
    )
}