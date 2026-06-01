package thong.kotlin.pomodoro.core.media

import javazoom.jl.player.Player
import java.io.BufferedInputStream
import java.io.InputStream
import kotlin.concurrent.thread

class JvmSoundManager private constructor() : SoundManager {
    private var player: Player? = null
    private var currentTrackId: String? = null
    private var isPlaying = false
    private var totalBytesRead = 0L
    private var pausedPosition = 0L

    companion object {
        val instance: JvmSoundManager by lazy { JvmSoundManager() }
    }

    override fun playAlarmSound() {}
    override fun playTickSound() {}

    override fun playBackgroundMusic(trackId: String) {
        if (currentTrackId == trackId) {
            if (isPlaying) return
            resumeBackgroundMusic()
            return
        }

        stopBackgroundMusic()
        pausedPosition = 0
        
        try {
            val resourcePath = "/composeResources/pomodrokotlin.shared.generated.resources/files/audio/$trackId.mp3"
            startPlayback(trackId, resourcePath)
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    private fun startPlayback(trackId: String, resourcePath: String) {
        isPlaying = true
        currentTrackId = trackId
        
        thread(start = true, isDaemon = true) {
            while (isPlaying && currentTrackId == trackId) {
                val inputStream = JvmSoundManager::class.java.getResourceAsStream(resourcePath)
                if (inputStream != null) {
                    val trackingStream = TrackingInputStream(inputStream)
                    if (pausedPosition > 0) {
                        trackingStream.skip(pausedPosition)
                    }
                    totalBytesRead = 0 // Reset for the new player instance
                    
                    val bufferedStream = BufferedInputStream(trackingStream)
                    player = Player(bufferedStream)
                    
                    try {
                        player?.play()
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                    
                    // If we reached the end normally, reset pausedPosition for loop
                    if (isPlaying && currentTrackId == trackId && player?.isComplete == true) {
                        pausedPosition = 0
                    }
                    
                    totalBytesRead = trackingStream.bytesRead
                } else {
                    println("Resource not found: $resourcePath")
                    break
                }
            }
        }
    }

    override fun resumeBackgroundMusic() {
        if (!isPlaying && currentTrackId != null) {
            val resourcePath = "/composeResources/pomodrokotlin.shared.generated.resources/files/audio/$currentTrackId.mp3"
            startPlayback(currentTrackId!!, resourcePath)
        }
    }

    override fun pauseBackgroundMusic() {
        if (isPlaying) {
            isPlaying = false
            // The Player might have read more bytes than it actually played due to buffering,
            // but this is a reasonable approximation for JLayer.
            player?.close()
            player = null
        }
    }

    override fun playBeepSound() {
        playEffect("beep_effect.wav")
    }

    override fun playChimeSound() {
        playEffect("time_over_effect.wav")
    }

    private fun playEffect(fileName: String) {
        thread(start = true, isDaemon = true) {
            try {
                val resourcePath = "/composeResources/pomodrokotlin.shared.generated.resources/files/audio/$fileName"
                val inputStream = JvmSoundManager::class.java.getResourceAsStream(resourcePath)
                if (inputStream != null) {
                    val bufferedStream = BufferedInputStream(inputStream)
                    
                    if (fileName.endsWith(".wav")) {
                        // Use Java Sound API for WAV files
                        val audioInputStream = javax.sound.sampled.AudioSystem.getAudioInputStream(bufferedStream)
                        val clip = javax.sound.sampled.AudioSystem.getClip()
                        clip.open(audioInputStream)
                        clip.start()
                        // Optional: listen for completion to close resources, but for short beeps it's usually fine
                    } else {
                        // Use JLayer for MP3
                        val player = Player(bufferedStream)
                        player.play()
                    }
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }

    override fun isBackgroundMusicPlaying(): Boolean {
        return isPlaying
    }

    override fun getCurrentTrackId(): String? {
        return currentTrackId
    }

    override fun getCurrentPosition(): Long {
        return pausedPosition // In JvmSoundManager, we track position in pausedPosition variable
    }

    override fun stopAllSounds() {
        stopBackgroundMusic()
        pausedPosition = 0
    }

    private fun stopBackgroundMusic() {
        isPlaying = false
        player?.close()
        player = null
        currentTrackId = null
        pausedPosition = 0
    }

    private class TrackingInputStream(private val inputStream: InputStream) : InputStream() {
        var bytesRead = 0L
            private set

        override fun read(): Int {
            val result = inputStream.read()
            if (result != -1) bytesRead++
            return result
        }

        override fun read(b: ByteArray, off: Int, len: Int): Int {
            val result = inputStream.read(b, off, len)
            if (result != -1) bytesRead += result
            return result
        }

        override fun skip(n: Long): Long {
            val result = inputStream.skip(n)
            bytesRead += result
            return result
        }

        override fun close() {
            inputStream.close()
        }
    }
}