package thong.kotlin.pomodoro

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import thong.kotlin.pomodoro.core.media.JvmSoundManager

fun main() = application {
    val soundManager = JvmSoundManager()
    
    Window(
        onCloseRequest = ::exitApplication,
        title = "Pomodrokotlin",
    ) {
        App(soundManager = soundManager)
    }
}