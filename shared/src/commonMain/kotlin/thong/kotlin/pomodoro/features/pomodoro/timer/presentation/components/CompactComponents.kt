package thong.kotlin.pomodoro.features.pomodoro.timer.presentation.components

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.List
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import thong.kotlin.pomodoro.core.designsystem.components.GlassBox
import thong.kotlin.pomodoro.core.designsystem.theme.AuraColors
import thong.kotlin.pomodoro.features.pomodoro.timer.domain.PomodoroMode
import thong.kotlin.pomodoro.features.pomodoro.timer.state.CompactSection

@Composable
fun CompactFloatingTimer(
    timeLeft: Long,
    mode: PomodoroMode,
    isActive: Boolean,
    onToggle: () -> Unit,
    modifier: Modifier = Modifier
) {
    val minutes = (timeLeft / 60).toString().padStart(2, '0')
    val seconds = (timeLeft % 60).toString().padStart(2, '0')
    
    val modeColor = when (mode) {
        PomodoroMode.WORK -> AuraColors.WorkMode
        PomodoroMode.SHORT_BREAK -> AuraColors.ShortBreakMode
        PomodoroMode.LONG_BREAK -> AuraColors.LongBreakMode
    }
    val modeText = when (mode) {
        PomodoroMode.WORK -> "WORK"
        PomodoroMode.SHORT_BREAK -> "BREAK"
        PomodoroMode.LONG_BREAK -> "REST"
    }

    GlassBox(
        modifier = modifier
            .width(140.dp)
            .height(64.dp),
        shape = RoundedCornerShape(20.dp),
        backgroundColor = AuraColors.BottomBarBackground.copy(alpha = 0.5f)
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = modeText,
                    color = modeColor,
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "$minutes:$seconds",
                    color = Color.White,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.ExtraBold
                )
            }
            
            IconButton(
                onClick = onToggle,
                modifier = Modifier
                    .size(36.dp)
                    .background(modeColor, CircleShape)
            ) {
                Icon(
                    imageVector = if (isActive) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (isActive) "Pause" else "Play",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun CompactMenu(
    isExpanded: Boolean,
    onToggleExpand: () -> Unit,
    onSelectSection: (CompactSection) -> Unit,
    onExitCompactMode: () -> Unit,
    modifier: Modifier = Modifier
) {
    Box(modifier = modifier, contentAlignment = Alignment.BottomEnd) {
        // Expandable Menu Panel
        AnimatedVisibility(
            visible = isExpanded,
            enter = fadeIn() + expandVertically(expandFrom = Alignment.Bottom),
            exit = fadeOut() + shrinkVertically(shrinkTowards = Alignment.Bottom)
        ) {
            GlassBox(
                modifier = Modifier
                    .padding(bottom = 72.dp)
                    .width(56.dp),
                shape = RoundedCornerShape(28.dp),
                backgroundColor = AuraColors.BottomBarBackground.copy(alpha = 0.7f)
            ) {
                Column(
                    modifier = Modifier.padding(vertical = 12.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    CompactMenuItem(Icons.AutoMirrored.Filled.List, "Tasks") { onSelectSection(CompactSection.TASKS) }
                    CompactMenuItem(Icons.Default.MusicNote, "Music") { onSelectSection(CompactSection.MUSIC) }
                    CompactMenuItem(Icons.Default.GraphicEq, "Ambient") { onSelectSection(CompactSection.AMBIENT) }
                    CompactMenuItem(Icons.Default.Image, "Backgrounds") { onSelectSection(CompactSection.BACKGROUND) }
                    CompactMenuItem(Icons.Default.Settings, "Settings") { onSelectSection(CompactSection.SETTINGS) }
                    
                    Spacer(modifier = Modifier.height(4.dp))
                    
                    // Exit Compact Mode Button
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(Color.White.copy(alpha = 0.15f), CircleShape)
                            .clickable { onExitCompactMode() },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.ZoomOutMap,
                            contentDescription = "Exit Compact",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }
            }
        }

        // Toggle FAB
        Box(
            modifier = Modifier
                .size(56.dp)
                .background(AuraColors.WorkMode, CircleShape)
                .clickable { onToggleExpand() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = if (isExpanded) Icons.Default.Close else Icons.Default.Menu,
                contentDescription = "Menu",
                tint = Color.White,
                modifier = Modifier.size(28.dp)
            )
        }
    }
}

@Composable
private fun CompactMenuItem(
    icon: ImageVector,
    label: String,
    onClick: () -> Unit
) {
    IconButton(
        onClick = onClick,
        modifier = Modifier.size(40.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = label,
            tint = Color.White.copy(alpha = 0.8f),
            modifier = Modifier.size(24.dp)
        )
    }
}

@Composable
fun CompactSectionOverlay(
    activeSection: CompactSection?,
    onClose: () -> Unit,
    content: @Composable (CompactSection) -> Unit
) {
    AnimatedVisibility(
        visible = activeSection != null,
        enter = fadeIn() + slideInVertically { it / 2 },
        exit = fadeOut() + slideOutVertically { it / 2 },
        modifier = Modifier.fillMaxSize()
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.3f))
                .clickable { onClose() },
            contentAlignment = Alignment.Center
        ) {
            GlassBox(
                modifier = Modifier
                    .padding(24.dp)
                    .widthIn(max = 500.dp)
                    .fillMaxHeight(0.85f)
                    .clickable(enabled = false) { },
                shape = RoundedCornerShape(32.dp),
                backgroundColor = AuraColors.BottomBarBackground.copy(alpha = 0.9f)
            ) {
                Column(modifier = Modifier.fillMaxSize()) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = when (activeSection) {
                                CompactSection.TASKS -> "Công việc"
                                CompactSection.MUSIC -> "Âm nhạc"
                                CompactSection.BACKGROUND -> "Hình nền"
                                CompactSection.AMBIENT -> "Âm thanh môi trường"
                                CompactSection.SETTINGS -> "Cài đặt"
                                null -> ""
                            },
                            color = Color.White,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold
                        )
                        IconButton(onClick = onClose) {
                            Icon(Icons.Default.Close, "Close", tint = Color.White)
                        }
                    }
                    
                    Box(modifier = Modifier.weight(1f).padding(start = 20.dp, end = 20.dp, bottom = 20.dp)) {
                        activeSection?.let { content(it) }
                    }
                }
            }
        }
    }
}
