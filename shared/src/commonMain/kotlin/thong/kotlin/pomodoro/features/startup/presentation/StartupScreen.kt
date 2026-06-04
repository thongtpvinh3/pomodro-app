package thong.kotlin.pomodoro.features.startup.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pomodrokotlin.shared.generated.resources.Res
import pomodrokotlin.shared.generated.resources.landspace_startup_bg
import thong.kotlin.pomodoro.core.designsystem.components.AuraBackground

@Composable
fun StartupScreen() {

    // AuraBackground đã hỗ trợ tự động đổi ảnh Landscape/Portrait và ContentScale.Crop (Full Fill)
    AuraBackground(
        landscapeImageRes = Res.drawable.landspace_startup_bg,
        blurRadius = 0f, 
        overlayAlpha = 0.2f
    ) {
        BoxWithConstraints(
            modifier = Modifier.fillMaxSize()
        ) {
            val isLandscape = maxWidth > maxHeight

            if (isLandscape) {
                // Landscape: Loading ở bên phải, Background đã tự động Full Fill và đổi ảnh phù hợp
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(end = 64.dp),
                    contentAlignment = Alignment.CenterEnd
                ) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(36.dp),
                        strokeWidth = 3.dp
                    )
                }
            } else {
                // Portrait: Loading ở dưới cùng
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(bottom = 64.dp),
                    contentAlignment = Alignment.BottomCenter
                ) {
                    CircularProgressIndicator(
                        color = Color.White,
                        modifier = Modifier.size(36.dp),
                        strokeWidth = 3.dp
                    )
                }
            }
        }
    }
}