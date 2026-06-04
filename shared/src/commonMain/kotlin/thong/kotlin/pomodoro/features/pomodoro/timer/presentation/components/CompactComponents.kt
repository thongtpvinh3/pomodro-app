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
import androidx.compose.ui.unit.Dp
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
    modifier: Modifier = Modifier,
    width: Dp = 140.dp,
    height: Dp = 64.dp,
    timeFontSize: androidx.compose.ui.unit.TextUnit = 20.sp,
    buttonSize: Dp = 36.dp,
    showExtraButtons: Boolean = false,
    onSettingsClick: () -> Unit = {},
    onExitClick: () -> Unit = {}
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

    Row(
        modifier = modifier.wrapContentSize(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (showExtraButtons) {
            IconButton(
                onClick = onExitClick,
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White.copy(alpha = 0.1f), CircleShape)
            ) {
                Icon(Icons.Default.ZoomOutMap, "Exit", tint = Color.White)
            }
        }

        GlassBox(
            modifier = Modifier
                .width(width)
                .height(height),
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
                        fontSize = (timeFontSize.value * 0.5).sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "$minutes:$seconds",
                        color = Color.White,
                        fontSize = timeFontSize,
                        fontWeight = FontWeight.ExtraBold
                    )
                }
                
                IconButton(
                    onClick = onToggle,
                    modifier = Modifier
                        .size(buttonSize)
                        .background(modeColor, CircleShape)
                ) {
                    Icon(
                        imageVector = if (isActive) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isActive) "Pause" else "Play",
                        tint = Color.White,
                        modifier = Modifier.size(buttonSize * 0.6f)
                    )
                }
            }
        }

        if (showExtraButtons) {
            IconButton(
                onClick = onSettingsClick,
                modifier = Modifier
                    .size(40.dp)
                    .background(Color.White.copy(alpha = 0.1f), CircleShape)
            ) {
                Icon(Icons.Default.Settings, "Settings", tint = Color.White)
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
    modifier: Modifier = Modifier,
    isLandscape: Boolean = false
) {
    Box(modifier = modifier, contentAlignment = Alignment.BottomEnd) {
        // Expandable Menu Panel
        AnimatedVisibility(
            visible = isExpanded,
            enter = if (isLandscape) fadeIn() + expandHorizontally(expandFrom = Alignment.End)
                    else fadeIn() + expandVertically(expandFrom = Alignment.Bottom),
            exit = if (isLandscape) fadeOut() + shrinkHorizontally(shrinkTowards = Alignment.End)
                   else fadeOut() + shrinkVertically(shrinkTowards = Alignment.Bottom)
        ) {
            GlassBox(
                modifier = if (isLandscape) {
                    Modifier.padding(end = 72.dp).height(56.dp)
                } else {
                    Modifier.padding(bottom = 72.dp).width(56.dp)
                },
                shape = RoundedCornerShape(28.dp),
                backgroundColor = AuraColors.BottomBarBackground.copy(alpha = 0.7f)
            ) {
                if (isLandscape) {
                    Row(
                        modifier = Modifier.padding(horizontal = 12.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        CompactMenuItem(Icons.AutoMirrored.Filled.List, "Tasks") { onSelectSection(CompactSection.TASKS) }
                        CompactMenuItem(Icons.Default.MusicNote, "Music") { onSelectSection(CompactSection.MUSIC) }
                        CompactMenuItem(Icons.Default.GraphicEq, "Ambient") { onSelectSection(CompactSection.AMBIENT) }
                        CompactMenuItem(Icons.Default.Image, "Backgrounds") { onSelectSection(CompactSection.BACKGROUND) }
                        
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color.White.copy(alpha = 0.15f), CircleShape)
                                .clickable { onExitCompactMode() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.ZoomOutMap, "Exit", tint = Color.White, modifier = Modifier.size(20.dp))
                        }
                    }
                } else {
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
                        
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color.White.copy(alpha = 0.15f), CircleShape)
                                .clickable { onExitCompactMode() },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(Icons.Default.ZoomOutMap, "Exit", tint = Color.White, modifier = Modifier.size(20.dp))
                        }
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
            BoxWithConstraints(
                modifier = Modifier
                    .padding(16.dp)
                    .widthIn(max = 500.dp)
                    .fillMaxWidth(0.95f)
                    .fillMaxHeight(0.85f)
            ) {
                GlassBox(
                    modifier = Modifier
                        .fillMaxSize()
                        .clickable(enabled = false) { },
                    shape = RoundedCornerShape(28.dp),
                    backgroundColor = AuraColors.BottomBarBackground.copy(alpha = 0.95f)
                ) {
                    Column(modifier = Modifier.fillMaxSize()) {
                        // Title Header inside the overlay
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 20.dp, vertical = 16.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = when (activeSection) {
                                    CompactSection.TASKS -> "Danh sách công việc"
                                    CompactSection.MUSIC -> "Nhạc tập trung"
                                    CompactSection.BACKGROUND -> "Đổi hình nền"
                                    CompactSection.AMBIENT -> "Âm thanh môi trường"
                                    CompactSection.SETTINGS -> "Cài đặt Timer"
                                    null -> ""
                                },
                                color = Color.White,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )

                            IconButton(
                                onClick = onClose,
                                modifier = Modifier.size(32.dp)
                            ) {
                                Icon(
                                    Icons.Default.Close,
                                    contentDescription = "Close",
                                    tint = Color.White.copy(alpha = 0.6f),
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .padding(start = 16.dp, end = 16.dp, bottom = 16.dp)
                        ) {
                            activeSection?.let { content(it) }
                        }
                    }
                }
            }
        }
    }
}
