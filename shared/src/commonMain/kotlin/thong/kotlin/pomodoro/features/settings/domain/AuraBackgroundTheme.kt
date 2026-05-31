package thong.kotlin.pomodoro.features.settings.domain

import org.jetbrains.compose.resources.DrawableResource
import pomodrokotlin.shared.generated.resources.Res
import pomodrokotlin.shared.generated.resources.cyberpunk_bg
import pomodrokotlin.shared.generated.resources.forest_bg
import pomodrokotlin.shared.generated.resources.ocean_bg
import pomodrokotlin.shared.generated.resources.startup_bg

enum class AuraBackgroundTheme(
    val id: String,
    val title: String,
    val imageRes: DrawableResource,
    val isPremium: Boolean,    // Cờ đánh dấu hàng bán trong Store
    val price: Int = 0         // Giá tiền (có thể là điểm Pomo Coins hoặc VNĐ)
) {
    // 2 mẫu mặc định miễn phí ban đầu
    DEFAULT("default", "Vũ trụ Huyền bí", Res.drawable.startup_bg, isPremium = false),
    MYSTIC_FOREST("forest", "Rừng sâu Tĩnh lặng", Res.drawable.forest_bg, isPremium = false),

    // Các mẫu phải mua từ Store
    OCEAN_BREEZE("ocean", "Đại dương Xanh", Res.drawable.ocean_bg, isPremium = true, price = 500),
    CYBERPUNK("cyber", "Thành phố Neon", Res.drawable.cyberpunk_bg, isPremium = true, price = 800)
}