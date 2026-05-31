package thong.kotlin.pomodoro.features.startup.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.delay
import org.jetbrains.compose.resources.painterResource
import pomodrokotlin.shared.generated.resources.Res
import pomodrokotlin.shared.generated.resources.startup_bg

@Composable
fun StartupScreen(onNavigateToHome: () -> Unit) {
    // LaunchedEffect sẽ chạy một coroutine khi Composable này được gọi lần đầu tiên.
    // Dùng để tạo độ trễ (delay) giả lập thời gian tải tài nguyên, sau đó chuyển màn hình.
    LaunchedEffect(key1 = true) {
        delay(3000L) // Chờ 2 giây
        onNavigateToHome()
    }

    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            painter = painterResource(Res.drawable.startup_bg),
            contentDescription = "Background Splash Screen",
            modifier = Modifier.fillMaxSize(),
            // ContentScale.Crop: Giúp ảnh tự động zoom và cắt phần thừa để lấp đầy toàn bộ màn hình của thiết bị mà không bị méo.
            contentScale = ContentScale.Crop
        )

        // 2. LỚP BÊN TRÊN (Tùy chọn): Vì ảnh của bạn đã có sẵn text rất đẹp,
        // chúng ta chỉ cần đè thêm một hiệu ứng Loading xoay xoay ở dưới cùng.
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 64.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Bottom
        ) {
            CircularProgressIndicator(
                color = Color.White,
                modifier = Modifier.size(36.dp),
                strokeWidth = 3.dp
            )
        }
    }
}