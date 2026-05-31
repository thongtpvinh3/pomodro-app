package thong.kotlin.pomodoro.core.designsystem.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import org.jetbrains.compose.resources.painterResource
import pomodrokotlin.shared.generated.resources.Res
import pomodrokotlin.shared.generated.resources.startup_bg

@Composable
fun AuraBackground(
    modifier: Modifier = Modifier,
    blurRadius: Float = 2f,
    haveBackground: Boolean = true,
    backgroundColor: Color = Color.Black,
    overlayAlpha: Float = 0.4f,
    content: @Composable () -> Unit
) {
    Box(
        modifier = modifier.fillMaxSize().background(backgroundColor)
    ) {
        if (haveBackground) {
            Image(
                painter = painterResource(Res.drawable.startup_bg),
                contentDescription = "Application Background",
                modifier = Modifier
                    .fillMaxSize()
                    .blur(blurRadius.dp),
                contentScale = ContentScale.Crop
            )
        }

        Box(
            modifier = Modifier.fillMaxSize().background(Color.Black.copy(alpha = overlayAlpha))
        )

        content()
    }
}