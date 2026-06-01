package thong.kotlin.pomodoro.features.pomodoro.timer.presentation

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
import thong.kotlin.pomodoro.core.designsystem.components.AuraBackground
import thong.kotlin.pomodoro.core.designsystem.theme.AuraColors
import thong.kotlin.pomodoro.features.pomodoro.task.TaskSection
import thong.kotlin.pomodoro.features.pomodoro.task.TaskBottomBar
import thong.kotlin.pomodoro.features.pomodoro.music.presentation.MusicSection
import thong.kotlin.pomodoro.features.pomodoro.timer.domain.EventType
import thong.kotlin.pomodoro.features.pomodoro.timer.domain.PomodoroMode
import thong.kotlin.pomodoro.features.pomodoro.timer.presentation.components.TimerSection
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

    AuraBackground(blurRadius = 3f, overlayAlpha = 0.4f) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 20.dp)
        ) {
            val isLandscape = maxWidth > maxHeight

            if (isLandscape) {
                LandscapePomodoroContent(
                    uiState = uiState,
                    themeColor = animatedThemeColor,
                    onToggleTimer = viewModel::toggleTimer,
                    onResetTimer = viewModel::resetTimer,
                    onSkipTimer = viewModel::skipTimer,
                    onAddTask = viewModel::addTask,
                    onDeleteTask = viewModel::deleteTask,
                    onToggleTask = viewModel::toggleTask,
                    onNewTaskTextChange = viewModel::onNewTaskTextChange,
                    onToggleMusic = viewModel::toggleMusic,
                    onSelectTrack = viewModel::selectTrack
                )
            } else {
                PortraitPomodoroContent(
                    uiState = uiState,
                    themeColor = animatedThemeColor,
                    onToggleTimer = viewModel::toggleTimer,
                    onResetTimer = viewModel::resetTimer,
                    onSkipTimer = viewModel::skipTimer,
                    onAddTask = viewModel::addTask,
                    onDeleteTask = viewModel::deleteTask,
                    onToggleTask = viewModel::toggleTask,
                    onNewTaskTextChange = viewModel::onNewTaskTextChange,
                    onToggleMusic = viewModel::toggleMusic,
                    onSelectTrack = viewModel::selectTrack,
                    onToggleTasksExpanded = viewModel::toggleTasksExpanded
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
    onAddTask: () -> Unit,
    onDeleteTask: (String) -> Unit,
    onToggleTask: (String) -> Unit,
    onNewTaskTextChange: (String) -> Unit,
    onToggleMusic: () -> Unit,
    onSelectTrack: (String) -> Unit,
    onToggleTasksExpanded: () -> Unit
) {
    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TimerSection(
                uiState = uiState,
                themeColor = themeColor,
                onToggleTimer = onToggleTimer,
                onResetTimer = onResetTimer,
                onSkipTimer = onSkipTimer,
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
            modifier = Modifier.align(Alignment.BottomCenter)
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
    onAddTask: () -> Unit,
    onDeleteTask: (String) -> Unit,
    onToggleTask: (String) -> Unit,
    onNewTaskTextChange: (String) -> Unit,
    onToggleMusic: () -> Unit,
    onSelectTrack: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 20.dp),
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

            TaskSection(
                tasks = uiState.tasks,
                newTaskText = uiState.newTaskText,
                onAddTask = onAddTask,
                onDeleteTask = onDeleteTask,
                onToggleTask = onToggleTask,
                onNewTaskTextChange = onNewTaskTextChange,
                showBreakEndBanner = uiState.event == EventType.BREAK_END,
                useLazyColumn = false,
                modifier = Modifier.fillMaxWidth()
            )
        }
    }
}