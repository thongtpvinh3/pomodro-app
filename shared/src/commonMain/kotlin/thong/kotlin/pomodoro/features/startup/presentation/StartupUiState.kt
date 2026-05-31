package thong.kotlin.pomodoro.features.startup.presentation

import thong.kotlin.pomodoro.features.startup.domain.NextDestination

sealed interface StartupUiState {
    data object Loading : StartupUiState
    data class Success(val destination: NextDestination) : StartupUiState
}

