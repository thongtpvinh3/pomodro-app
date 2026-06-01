package thong.kotlin.pomodoro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat

import thong.kotlin.pomodoro.core.media.AndroidSoundManager

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        enableEdgeToEdge()
        super.onCreate(savedInstanceState)

        val soundManager = AndroidSoundManager.getInstance(this)
        
        // Yêu cầu hệ thống cho phép ứng dụng vẽ tràn viền
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val windowInsetsController = WindowCompat.getInsetsController(window, window.decorView)

        // Ẩn thanh điều hướng ở dưới cùng
        windowInsetsController.hide(WindowInsetsCompat.Type.navigationBars())

        // (Tùy chọn) Cho phép người dùng vuốt từ mép dưới lên để hiển thị lại thanh này trong chốc lát mà không làm xô lệch giao diện
        windowInsetsController.systemBarsBehavior =
            WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE

        setContent {
            App(soundManager = soundManager)
        }
    }
}

@Preview
@Composable
fun AppAndroidPreview() {
    App()
}