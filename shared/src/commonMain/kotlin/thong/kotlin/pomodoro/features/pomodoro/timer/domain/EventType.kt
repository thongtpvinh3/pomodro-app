package thong.kotlin.pomodoro.features.pomodoro.timer.domain

enum class EventType {
    CLICK_START_WORK,
    CLICK_PAUSE_WORK,
    WORK_END,
    CLICK_START_BREAK,
    CLICK_PAUSE_BREAK,
    BREAK_END,
    NOTHING
}