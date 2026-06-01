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
import thong.kotlin.pomodoro.features.pomodoro.timer.presentation.PomodoroScreenResponsive
import thong.kotlin.pomodoro.features.pomodoro.timer.state.PomodoroUiState
import thong.kotlin.pomodoro.features.pomodoro.timer.state.PomodoroUiStateSaver
import thong.kotlin.pomodoro.features.pomodoro.timer.viewmodel.PomodoroViewModel

@Composable
@Preview
fun App(soundManager: thong.kotlin.pomodoro.core.media.SoundManager? = null) {
    val coroutineScope = rememberCoroutineScope()
    var currentScreenName by rememberSaveable {
        mutableStateOf("Splash")
    }
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
            soundManager = soundManager,
            initialState = savedPomodoroState
        )
    }
    LaunchedEffect(Unit) {
        // Restore music if it was playing
        if (savedPomodoroState.isMusicPlaying) {
            savedPomodoroState.selectedTrackId?.let { trackId ->
                soundManager?.playBackgroundMusic(trackId)
            }
        }

        pomodoroViewModel.uiState.collect {
            savedPomodoroState = it
        }
    }

    AuraTheme {
        AuraNavigator(currentScreen = currentScreen) { screen ->
            when (screen) {
                is AuraScreen.Splash -> {
                    LaunchedEffect(Unit) {
                        delay(5000)
                        currentScreenName = "Onboarding"
                    }
                    StartupScreen()
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