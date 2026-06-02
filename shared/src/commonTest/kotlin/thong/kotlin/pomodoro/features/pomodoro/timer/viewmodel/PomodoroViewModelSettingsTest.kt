package thong.kotlin.pomodoro.features.pomodoro.timer.viewmodel

import kotlinx.coroutines.test.runTest
import thong.kotlin.pomodoro.features.pomodoro.timer.state.PomodoroUiState
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertNotNull
import kotlin.test.assertTrue

class PomodoroViewModelSettingsTest {

    @Test
    fun `test toggle settings pre-fills values`() = runTest {
        val viewModel = PomodoroViewModel(this)
        
        assertFalse(viewModel.uiState.value.isSettingsVisible)
        
        viewModel.toggleSettings()
        
        assertTrue(viewModel.uiState.value.isSettingsVisible)
        assertEquals("1", viewModel.uiState.value.editingWorkMinutes) // Default is 1 in PomodoroConfig
        assertEquals("1", viewModel.uiState.value.editingBreakMinutes)
    }

    @Test
    fun `test save settings updates config and resets timer`() = runTest {
        val viewModel = PomodoroViewModel(this)
        
        viewModel.toggleSettings()
        viewModel.onWorkMinutesChange("30")
        viewModel.onBreakMinutesChange("10")
        
        viewModel.saveSettings()
        
        assertFalse(viewModel.uiState.value.isSettingsVisible)
        assertEquals(30, viewModel.uiState.value.config.workMinutes)
        assertEquals(10, viewModel.uiState.value.config.shortBreakMinutes)
        assertEquals(30 * 60L, viewModel.uiState.value.timeLeft)
    }

    @Test
    fun `test invalid settings show error`() = runTest {
        val viewModel = PomodoroViewModel(this)
        
        viewModel.toggleSettings()
        viewModel.onWorkMinutesChange("500") // Invalid
        
        viewModel.saveSettings()
        
        assertTrue(viewModel.uiState.value.isSettingsVisible)
        assertNotNull(viewModel.uiState.value.settingsError)
        assertEquals(1, viewModel.uiState.value.config.workMinutes) // Should not update
    }
}
