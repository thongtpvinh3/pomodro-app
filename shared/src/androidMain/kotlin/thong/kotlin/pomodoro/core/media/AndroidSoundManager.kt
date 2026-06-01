package thong.kotlin.pomodoro.core.media

import android.content.Context
import android.media.MediaPlayer

class AndroidSoundManager(private val context: Context) : SoundManager {
    private var mediaPlayer: MediaPlayer? = null
    private var currentTrackId: String? = null

    override fun playAlarmSound() {
        // Triển khai sau nếu cần
    }

    override fun playTickSound() {
        // Triển khai sau nếu cần
    }

    override fun playBackgroundMusic(trackId: String) {
        if (currentTrackId == trackId) {
            if (mediaPlayer != null) {
                if (mediaPlayer?.isPlaying == false) {
                    mediaPlayer?.start()
                }
                return
            }
        }

        stopBackgroundMusic()
        
        try {
            val assetPath = "composeResources/pomodrokotlin.shared.generated.resources/files/audio/$trackId.mp3"
            val assetDescriptor = context.assets.openFd(assetPath)
            
            mediaPlayer = MediaPlayer().apply {
                setDataSource(assetDescriptor.fileDescriptor, assetDescriptor.startOffset, assetDescriptor.length)
                assetDescriptor.close()
                prepare()
                isLooping = true // Ensure looping is set
                start()
            }
            currentTrackId = trackId
        } catch (e: Exception) {
            android.util.Log.e("AndroidSoundManager", "Error playing music: $trackId", e)
        }
    }

    override fun resumeBackgroundMusic() {
        if (mediaPlayer != null && mediaPlayer?.isPlaying == false) {
            mediaPlayer?.start()
        }
    }

    override fun pauseBackgroundMusic() {
        mediaPlayer?.pause()
    }

    override fun playBeepSound() {
        playShortEffect("beep_effect")
    }

    override fun playChimeSound() {
        playShortEffect("time_over_effect")
    }

    private fun playShortEffect(fileName: String) {
        try {
            val assetPath = "composeResources/pomodrokotlin.shared.generated.resources/files/audio/$fileName.wav"
            val assetDescriptor = context.assets.openFd(assetPath)
            MediaPlayer().apply {
                setDataSource(assetDescriptor.fileDescriptor, assetDescriptor.startOffset, assetDescriptor.length)
                assetDescriptor.close()
                prepare()
                setOnCompletionListener { it.release() }
                start()
            }
        } catch (e: Exception) {
            android.util.Log.e("AndroidSoundManager", "Error playing effect: $fileName", e)
        }
    }

    override fun stopAllSounds() {
        stopBackgroundMusic()
    }

    private fun stopBackgroundMusic() {
        mediaPlayer?.stop()
        mediaPlayer?.release()
        mediaPlayer = null
        currentTrackId = null
    }
}