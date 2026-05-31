package thong.kotlin.pomodoro.core.designsystem.components

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
     * Dừng toàn bộ âm thanh đang phát
     */
    fun stopAllSounds()
}