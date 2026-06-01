package thong.kotlin.pomodoro.core.media

import android.content.Context
import android.media.MediaPlayer

class AndroidSoundManager private constructor(context: Context) : SoundManager {
    private val context = context.applicationContext
    private var mediaPlayer: MediaPlayer? = null
    private var currentTrackId: String? = null

    companion object {
        @Volatile
        private var instance: AndroidSoundManager? = null

        fun getInstance(context: Context): AndroidSoundManager {
            return instance ?: synchronized(this) {
                instance ?: AndroidSoundManager(context).also { instance = it }
            }
        }
    }

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

    override fun isBackgroundMusicPlaying(): Boolean {
        return mediaPlayer?.isPlaying ?: false
    }

    override fun getCurrentTrackId(): String? {
        return currentTrackId
    }

    override fun getCurrentPosition(): Long {
        return mediaPlayer?.currentPosition?.toLong() ?: 0L
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