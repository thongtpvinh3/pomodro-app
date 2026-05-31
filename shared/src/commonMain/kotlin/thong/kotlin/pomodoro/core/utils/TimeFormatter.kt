package thong.kotlin.pomodoro.core.utils

/**
 * Định dạng số giây (Long) thành định dạng chuỗi hiển thị MM:SS
 * Ví dụ: 65L -> "01:05"
 */
fun Long.formatToMmSs(): String {
    val minutes = this / 60
    val seconds = this % 60

    val minString = minutes.toString().padStart(2, '0')
    val secString = seconds.toString().padStart(2, '0')

    return "$minString:$secString"
}