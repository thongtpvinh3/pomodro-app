package thong.kotlin.pomodoro.features.pomodoro.presentation

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import pomodrokotlin.shared.generated.resources.Res
import pomodrokotlin.shared.generated.resources.startup_bg
import thong.kotlin.pomodoro.core.designsystem.components.AuraBackground
import thong.kotlin.pomodoro.core.designsystem.theme.AuraColors
import thong.kotlin.pomodoro.features.pomodoro.task.presentation.components.TaskBottomBar
import thong.kotlin.pomodoro.features.pomodoro.task.presentation.components.TaskSideBar
import thong.kotlin.pomodoro.features.pomodoro.music.presentation.MusicSection
import thong.kotlin.pomodoro.features.pomodoro.timer.domain.PomodoroMode
import thong.kotlin.pomodoro.features.pomodoro.timer.presentation.components.BackgroundSection
import thong.kotlin.pomodoro.features.pomodoro.ambient.presentation.components.AmbientSoundSection
import thong.kotlin.pomodoro.features.pomodoro.timer.presentation.components.TimerSection
import thong.kotlin.pomodoro.features.pomodoro.timer.presentation.components.PomodoroSettingsModal
import thong.kotlin.pomodoro.features.pomodoro.timer.state.PomodoroUiState
import thong.kotlin.pomodoro.features.pomodoro.timer.viewmodel.PomodoroViewModel

@Composable
fun PomodoroScreenResponsive(viewModel: PomodoroViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    val animatedThemeColor by animateColorAsState(
        targetValue = when (uiState.currentMode) {
            PomodoroMode.WORK -> AuraColors.WorkMode
            PomodoroMode.SHORT_BREAK -> AuraColors.ShortBreakMode
            PomodoroMode.LONG_BREAK -> AuraColors.LongBreakMode
        },
        animationSpec = tween(durationMillis = 500),
        label = "ThemeColorTransition",
    )

    val currentBackground = uiState.availableBackgrounds.find { it.id == uiState.selectedBackgroundId }

    AuraBackground(
        imageRes = currentBackground?.resource ?: Res.drawable.startup_bg,
        landscapeImageRes = currentBackground?.landscapeResource,
        blurRadius = 3f,
        overlayAlpha = 0.4f
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            BoxWithConstraints(
                modifier = Modifier
                    .fillMaxSize()
            ) {
                val isLandscape = maxWidth > maxHeight

                if (isLandscape) {
                    LandscapePomodoroContent(
                        uiState = uiState,
                        themeColor = animatedThemeColor,
                        onToggleTimer = viewModel::toggleTimer,
                        onResetTimer = viewModel::resetTimer,
                        onSkipTimer = viewModel::skipTimer,
                        onToggleSettings = viewModel::toggleSettings,
                        onAddTask = viewModel::addTask,
                        onDeleteTask = viewModel::deleteTask,
                        onToggleTask = viewModel::toggleTask,
                        onNewTaskTextChange = viewModel::onNewTaskTextChange,
                        onToggleMusic = viewModel::toggleMusic,
                        onSelectTrack = viewModel::selectTrack,
                        onToggleAmbientSound = viewModel::toggleAmbientSound,
                        onToggleTasksExpanded = viewModel::toggleTasksExpanded,
                        onSelectBackground = viewModel::selectBackground
                    )
                } else {
                    PortraitPomodoroContent(
                        uiState = uiState,
                        themeColor = animatedThemeColor,
                        onToggleTimer = viewModel::toggleTimer,
                        onResetTimer = viewModel::resetTimer,
                        onSkipTimer = viewModel::skipTimer,
                        onToggleSettings = viewModel::toggleSettings,
                        onAddTask = viewModel::addTask,
                        onDeleteTask = viewModel::deleteTask,
                        onToggleTask = viewModel::toggleTask,
                        onNewTaskTextChange = viewModel::onNewTaskTextChange,
                        onToggleMusic = viewModel::toggleMusic,
                        onSelectTrack = viewModel::selectTrack,
                        onToggleAmbientSound = viewModel::toggleAmbientSound,
                        onToggleTasksExpanded = viewModel::toggleTasksExpanded,
                        onSelectBackground = viewModel::selectBackground
                    )
                }
            }

            // Settings Modal
            if (uiState.isSettingsVisible) {
                PomodoroSettingsModal(
                    uiState = uiState,
                    onWorkChange = viewModel::onWorkMinutesChange,
                    onBreakChange = viewModel::onBreakMinutesChange,
                    onSave = viewModel::saveSettings,
                    onCancel = viewModel::toggleSettings,
                    onReset = viewModel::resetSettingsToDefault
                )
            }
        }
    }
}

@Composable
private fun PortraitPomodoroContent(
    uiState: PomodoroUiState,
    themeColor: Color,
    onToggleTimer: () -> Unit,
    onResetTimer: () -> Unit,
    onSkipTimer: () -> Unit,
    onToggleSettings: () -> Unit,
    onAddTask: () -> Unit,
    onDeleteTask: (String) -> Unit,
    onToggleTask: (String) -> Unit,
    onNewTaskTextChange: (String) -> Unit,
    onToggleMusic: () -> Unit,
    onSelectTrack: (String) -> Unit,
    onToggleAmbientSound: (String) -> Unit,
    onToggleTasksExpanded: () -> Unit,
    onSelectBackground: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimerSection(
                uiState = uiState,
                themeColor = themeColor,
                onToggleTimer = onToggleTimer,
                onResetTimer = onResetTimer,
                onSkipTimer = onSkipTimer,
                onToggleSettings = onToggleSettings,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            MusicSection(
                availableTracks = uiState.availableTracks,
                selectedTrackId = uiState.selectedTrackId,
                isMusicPlaying = uiState.isMusicPlaying,
                onToggleMusic = onToggleMusic,
                onSelectTrack = onSelectTrack,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            BackgroundSection(
                availableBackgrounds = uiState.availableBackgrounds,
                selectedBackgroundId = uiState.selectedBackgroundId,
                onSelectBackground = onSelectBackground,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(24.dp))

            AmbientSoundSection(
                availableSounds = uiState.availableAmbientSounds,
                activeSoundIds = uiState.activeAmbientSoundIds,
                onToggleSound = onToggleAmbientSound,
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(100.dp)) // Extra space for bottom bar
        }

        TaskBottomBar(
            tasks = uiState.tasks,
            isExpanded = uiState.isTasksExpanded,
            onToggleExpand = onToggleTasksExpanded,
            newTaskText = uiState.newTaskText,
            onAddTask = onAddTask,
            onDeleteTask = onDeleteTask,
            onToggleTask = onToggleTask,
            onNewTaskTextChange = onNewTaskTextChange,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp)
                .align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun LandscapePomodoroContent(
    uiState: PomodoroUiState,
    themeColor: Color,
    onToggleTimer: () -> Unit,
    onResetTimer: () -> Unit,
    onSkipTimer: () -> Unit,
    onToggleSettings: () -> Unit,
    onAddTask: () -> Unit,
    onDeleteTask: (String) -> Unit,
    onToggleTask: (String) -> Unit,
    onNewTaskTextChange: (String) -> Unit,
    onToggleMusic: () -> Unit,
    onSelectTrack: (String) -> Unit,
    onToggleAmbientSound: (String) -> Unit,
    onToggleTasksExpanded: () -> Unit,
    onSelectBackground: (String) -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 20.dp, bottom = 20.dp, start = 40.dp, end = 92.dp),
            verticalAlignment = Alignment.Top,
            horizontalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                TimerSection(
                    uiState = uiState,
                    themeColor = themeColor,
                    onToggleTimer = onToggleTimer,
                    onResetTimer = onResetTimer,
                    onSkipTimer = onSkipTimer,
                    onToggleSettings = onToggleSettings,
                    compact = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .verticalScroll(rememberScrollState())
            ) {
                MusicSection(
                    availableTracks = uiState.availableTracks,
                    selectedTrackId = uiState.selectedTrackId,
                    isMusicPlaying = uiState.isMusicPlaying,
                    onToggleMusic = onToggleMusic,
                    onSelectTrack = onSelectTrack,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                BackgroundSection(
                    availableBackgrounds = uiState.availableBackgrounds,
                    selectedBackgroundId = uiState.selectedBackgroundId,
                    onSelectBackground = onSelectBackground,
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                AmbientSoundSection(
                    availableSounds = uiState.availableAmbientSounds,
                    activeSoundIds = uiState.activeAmbientSoundIds,
                    onToggleSound = onToggleAmbientSound,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        }

        TaskSideBar(
            tasks = uiState.tasks,
            isExpanded = uiState.isTasksExpanded,
            onToggleExpand = onToggleTasksExpanded,
            newTaskText = uiState.newTaskText,
            onAddTask = onAddTask,
            onDeleteTask = onDeleteTask,
            onToggleTask = onToggleTask,
            onNewTaskTextChange = onNewTaskTextChange,
            modifier = Modifier.align(Alignment.BottomEnd)
        )
    }
}
