package thong.kotlin.pomodoro.features.pomodoro.ambient.data

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.Air
import androidx.compose.material.icons.filled.Fireplace
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Thunderstorm
import androidx.compose.material.icons.filled.WaterDrop
import thong.kotlin.pomodoro.features.pomodoro.ambient.domain.AmbientSound

object AmbientSoundRepository {
    val availableSounds: List<AmbientSound> = listOf(
        AmbientSound("rain", "Tiếng mưa", Icons.Default.WaterDrop),
        AmbientSound("storm", "Bão tố", Icons.Default.Thunderstorm),
        AmbientSound("campfire", "Lửa trại", Icons.Default.Fireplace),
        AmbientSound("library", "Thư viện", Icons.AutoMirrored.Filled.MenuBook),
        AmbientSound("white_noise", "Tiếng ồn trắng", Icons.Default.Air)
    )
}
