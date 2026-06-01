package thong.kotlin.pomodoro.core.media

/**
 * Bản hợp đồng quản lý âm thanh dùng chung cho toàn bộ dự án KMP
 */
interface SoundManager {
    /**
     * Phát tiếng chuông báo thức khi hoàn thành phiên học/nghỉ
     */
    fun playAlarmSound()

    /**
     * Phát tiếng gõ tích tắc nhẹ nhàng kích thích tập trung (Metronome hiệu ứng)
     */
    fun playTickSound()

    /**
     * Phát nhạc nền theo ID bài hát
     */
    fun playBackgroundMusic(trackId: String)

    /**
     * Tiếp tục phát nhạc nền (nếu đang tạm dừng)
     */
    fun resumeBackgroundMusic()

    /**
     * Tạm dừng nhạc nền
     */
    fun pauseBackgroundMusic()

    /**
     * Phát tiếng bíp ngắn (dùng cho 5 giây cuối)
     */
    fun playBeepSound()

    /**
     * Phát tiếng chuông báo hoàn thành (chime)
     */
    fun playChimeSound()

    /**
     * Dừng toàn bộ âm thanh đang phát
     */
    fun stopAllSounds()
}