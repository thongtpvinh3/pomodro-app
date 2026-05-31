package thong.kotlin.pomodoro.features.onboarding.presentation

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import thong.kotlin.pomodoro.core.designsystem.AuraColors
import thong.kotlin.pomodoro.core.designsystem.components.AuraBackground
import thong.kotlin.pomodoro.core.designsystem.components.AuraButton
import thong.kotlin.pomodoro.core.designsystem.components.GlassBox

// 1. Định nghĩa cấu trúc dữ liệu cho từng bước hướng dẫn
private data class OnboardingStep(
    val emoji: String,
    val title: String,
    val description: String,
    val highlightColor: Color
)

@Composable
fun OnboardingScreen(
    onFinish: () -> Unit,
    modifier: Modifier = Modifier
) {
    val steps = remember {
        listOf(
            OnboardingStep(
                emoji = "🎯",
                title = "Chào mừng đến với Aura Pomo",
                description = "Phương pháp Pomodoro giúp bạn làm việc sâu và hiệu quả hơn bằng cách chia nhỏ thời gian thành các khoảng tập trung cao độ.",
                highlightColor = AuraColors.WorkMode
            ),
            OnboardingStep(
                emoji = "⚡",
                title = "Tập trung & Nghỉ ngơi",
                description = "25 phút tập trung hết mình cho công việc, sau đó thưởng cho bản thân 5 phút nghỉ ngơi để não bộ tái tạo năng lượng.",
                highlightColor = AuraColors.ShortBreakMode
            ),
            OnboardingStep(
                emoji = "✨",
                title = "Quản lý công việc dễ dàng",
                description = "Theo dõi và gạch bỏ những mục tiêu cần hoàn thành trong ngày ngay tại phòng học để luôn làm chủ tiến độ của bản thân.",
                highlightColor = AuraColors.MainAppMode ?: AuraColors.LongBreakMode
            )
        )
    }

    var currentStep by remember { mutableStateOf(0) }
    val currentData = steps[currentStep]

    // SỬ DỤNG: AuraBackground từ core với độ blur cao hơn (8dp) và nền tối hơn (60%)
    // để người dùng không bị phân tâm bởi hình nền phía sau
    AuraBackground(blurRadius = 8f, overlayAlpha = 0.6f) {

        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
                .padding(top = 40.dp, bottom = 48.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {

            // TẦNG TRÊN: Nút Bỏ qua (Skip) dành cho người dùng cũ muốn vào thẳng app
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Bỏ qua",
                    color = AuraColors.TextSecondary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    modifier = Modifier
                        .padding(8.dp)
                        .clickable { onFinish() } // Gọi callback kết thúc luồng
                )
            }

            // TẦNG GIỮA: Nội dung hướng dẫn được bọc trong GlassBox và có hiệu ứng lướt trang mượt mà
            AnimatedContent(
                targetState = currentData,
                transitionSpec = {
                    fadeIn(animationSpec = androidx.compose.animation.core.tween(300)) togetherWith
                            fadeOut(animationSpec = androidx.compose.animation.core.tween(300))
                },
                label = "OnboardingStepTransition",
                modifier = Modifier.weight(1f).wrapContentHeight(Alignment.CenterVertically)
            ) { data ->
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    // Vòng tròn chứa Emoji đại diện cho tính năng (SỬ DỤNG: GlassBox hình tròn)
                    GlassBox(
                        shape = CircleShape,
                        modifier = Modifier.size(110.dp)
                    ) {
                        Text(text = data.emoji, fontSize = 44.sp)
                    }

                    Spacer(modifier = Modifier.height(32.dp))

                    // Tiêu đề bước hướng dẫn
                    Text(
                        text = data.title,
                        color = AuraColors.TextPrimary,
                        style = MaterialTheme.typography.titleLarge,
                        textAlign = TextAlign.Center
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    // Đoạn văn miêu tả chi tiết
                    Text(
                        text = data.description,
                        color = AuraColors.TextSecondary,
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        lineHeight = 22.sp,
                        modifier = Modifier.padding(horizontal = 8.dp)
                    )
                }
            }

            // TẦNG DƯỚI: Thanh chỉ báo (Dots Indicator) và Nút bấm hành động
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.fillMaxWidth()
            ) {
                // 1. Dấu chấm chỉ số trang (Dots Indicator)
                Row(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 32.dp)
                ) {
                    steps.forEachIndexed { index, step ->
                        val isSelected = index == currentStep
                        // Nếu chọn thì thanh sẽ dài ra và đổi theo màu đặc trưng của bước đó
                        val width = if (isSelected) 24.dp else 8.dp
                        val color = if (isSelected) step.highlightColor else Color.White.copy(alpha = 0.2f)

                        Box(
                            modifier = Modifier
                                .size(height = 8.dp, width = width)
                                .background(color, RoundedCornerShape(50))
                                .animateContentSize() // Hiệu ứng giãn chấm mượt mà
                        )
                    }
                }

                // 2. Nút bấm hành động (SỬ DỤNG: AuraButton co giãn vật lý từ core)
                AuraButton(
                    onClick = {
                        if (currentStep < steps.size - 1) {
                            currentStep++ // Đi tiếp trang sau
                        } else {
                            onFinish() // Đã đến trang cuối, kích hoạt vào thẳng MainApp
                        }
                    },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text(
                        text = if (currentStep == steps.size - 1) "Bắt đầu ngay" else "Tiếp tục",
                        color = if (currentStep == steps.size - 1) currentData.highlightColor else Color.White,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}