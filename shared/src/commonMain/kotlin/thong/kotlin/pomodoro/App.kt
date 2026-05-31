package thong.kotlin.pomodoro

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import thong.kotlin.pomodoro.core.designsystem.AuraNavigator
import thong.kotlin.pomodoro.core.designsystem.AuraScreen
import thong.kotlin.pomodoro.core.designsystem.AuraTheme
import thong.kotlin.pomodoro.features.onboarding.presentation.OnboardingScreen
import thong.kotlin.pomodoro.features.startup.presentation.StartupScreen
import thong.kotlin.pomodoro.features.timer.presentation.PomodoroScreen
import thong.kotlin.pomodoro.features.timer.presentation.PomodoroViewModel

@Composable
@Preview
fun App() {

    // Quản lý màn hình hiện tại của toàn app bằng cấu trúc mã Core điều hướng
    var currentScreen by remember { mutableStateOf<AuraScreen>(AuraScreen.Splash) }
    var showStartupScreen by remember { mutableStateOf(true) }

    // Bọc toàn bộ trong chiếc áo khoác AuraTheme chung
    AuraTheme {
        AuraNavigator(currentScreen = currentScreen) { screen ->
            when (screen) {
                is AuraScreen.Splash -> {
                    LaunchedEffect(Unit) {
                        delay(3000)
                        currentScreen = AuraScreen.Onboarding
                    }
                    StartupScreen(onNavigateToHome = { showStartupScreen = false })
                }

                is AuraScreen.Onboarding -> {
                    OnboardingScreen(onFinish = {
                        currentScreen = AuraScreen.MainApp
                    })
                }

                is AuraScreen.MainApp -> {
                    // Tạo một CoroutineScope gắn liền với vòng đời của Composable App
                    val coroutineScope = rememberCoroutineScope()

                    // Khởi tạo ViewModel và giữ trạng thái (remember) để không bị tạo lại khi recompose
                    val viewModel = remember { PomodoroViewModel(coroutineScope) }

                    // Gọi màn hình Pomodoro đã code ở bước trước vào đây
                    PomodoroScreen(viewModel = viewModel)
                }
            }
        }
    }
}