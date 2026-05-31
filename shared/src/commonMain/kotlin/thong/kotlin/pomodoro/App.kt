package thong.kotlin.pomodoro

import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.delay
import thong.kotlin.pomodoro.core.navigation.AuraNavigator
import thong.kotlin.pomodoro.core.navigation.AuraScreen
import thong.kotlin.pomodoro.core.designsystem.theme.AuraTheme
import thong.kotlin.pomodoro.features.onboarding.presentation.OnboardingScreen
import thong.kotlin.pomodoro.features.startup.presentation.StartupScreen
import thong.kotlin.pomodoro.features.timer.presentation.PomodoroScreenResponsive
import thong.kotlin.pomodoro.features.timer.model.PomodoroUiState
import thong.kotlin.pomodoro.features.timer.state.PomodoroUiStateSaver
import thong.kotlin.pomodoro.features.timer.viewmodel.PomodoroViewModel

@Composable
@Preview
fun App() {
    val coroutineScope = rememberCoroutineScope()
    var currentScreenName by rememberSaveable {
        mutableStateOf("Splash")
    }
    var showStartupScreen by remember { mutableStateOf(false) }
    var currentScreen by remember(currentScreenName) {
        mutableStateOf(
            when (currentScreenName) {
                "Onboarding" -> AuraScreen.Onboarding
                "MainApp" -> AuraScreen.MainApp
                else -> AuraScreen.Splash
            }
        )
    }
    var savedPomodoroState by rememberSaveable(
        stateSaver = PomodoroUiStateSaver
    ) {
        mutableStateOf(PomodoroUiState())
    }
    val pomodoroViewModel = remember {
        PomodoroViewModel(
            viewModelScope = coroutineScope,
            initialState = savedPomodoroState
        )
    }
    LaunchedEffect(Unit) {
        pomodoroViewModel.uiState.collect {
            savedPomodoroState = it
        }
    }

    AuraTheme {
        AuraNavigator(currentScreen = currentScreen) { screen ->
            when (screen) {
                is AuraScreen.Splash -> {
                    LaunchedEffect(Unit) {
                        delay(3000)
                        currentScreenName = "Onboarding"
                    }
                    StartupScreen(onNavigateToHome = { showStartupScreen = false })
                }
                is AuraScreen.Onboarding -> {
                    OnboardingScreen(onFinish = {
                        currentScreenName = "MainApp"
                    })
                }
                is AuraScreen.MainApp -> {
                    PomodoroScreenResponsive(viewModel = pomodoroViewModel)
                }
            }
        }
    }
}