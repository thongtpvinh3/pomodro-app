package thong.kotlin.pomodoro.features.pomodoro.data.local

import com.russhwolf.settings.Settings
import com.russhwolf.settings.ObservableSettings
import com.russhwolf.settings.set
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import thong.kotlin.pomodoro.features.pomodoro.domain.model.UserSettings

class LocalSettingsDataSource(private val settings: Settings) {

    companion object {
        private const val KEY_WORK_MINUTES = "work_minutes"
        private const val KEY_BREAK_MINUTES = "break_minutes"
        private const val KEY_LONG_BREAK_MINUTES = "long_break_minutes"
        private const val KEY_AUTO_START_BREAK = "auto_start_break"
        private const val KEY_AUTO_START_WORK = "auto_start_work"
        private const val KEY_SELECTED_BACKGROUND_ID = "selected_background_id"
        private const val KEY_NOTIFICATION_ENABLED = "notification_enabled"
        private const val KEY_SOUND_ENABLED = "sound_enabled"
        private const val KEY_VIBRATION_ENABLED = "vibration_enabled"
        private const val KEY_COMPACT_MODE = "compact_mode"
        private const val KEY_MINIMAL_MODE = "minimal_mode"
        private const val KEY_BATTERY_SAVER = "battery_saver"
        private const val KEY_HAS_COMPLETED_ONBOARDING = "has_completed_onboarding"
    }

    fun getUserSettings(): UserSettings {
        return UserSettings(
            workMinutes = settings.getInt(KEY_WORK_MINUTES, 25),
            breakMinutes = settings.getInt(KEY_BREAK_MINUTES, 5),
            longBreakMinutes = settings.getInt(KEY_LONG_BREAK_MINUTES, 15),
            autoStartBreak = settings.getBoolean(KEY_AUTO_START_BREAK, false),
            autoStartWork = settings.getBoolean(KEY_AUTO_START_WORK, false),
            selectedBackgroundId = settings.getStringOrNull(KEY_SELECTED_BACKGROUND_ID),
            isNotificationEnabled = settings.getBoolean(KEY_NOTIFICATION_ENABLED, true),
            isSoundEnabled = settings.getBoolean(KEY_SOUND_ENABLED, true),
            isVibrationEnabled = settings.getBoolean(KEY_VIBRATION_ENABLED, true),
            isCompactMode = settings.getBoolean(KEY_COMPACT_MODE, false),
            isMinimalMode = settings.getBoolean(KEY_MINIMAL_MODE, false),
            isBatterySaverEnabled = settings.getBoolean(KEY_BATTERY_SAVER, false),
            hasCompletedOnboarding = settings.getBoolean(KEY_HAS_COMPLETED_ONBOARDING, false)
        )
    }

    fun saveUserSettings(userSettings: UserSettings) {
        settings[KEY_WORK_MINUTES] = userSettings.workMinutes
        settings[KEY_BREAK_MINUTES] = userSettings.breakMinutes
        settings[KEY_LONG_BREAK_MINUTES] = userSettings.longBreakMinutes
        settings[KEY_AUTO_START_BREAK] = userSettings.autoStartBreak
        settings[KEY_AUTO_START_WORK] = userSettings.autoStartWork
        settings[KEY_SELECTED_BACKGROUND_ID] = userSettings.selectedBackgroundId
        settings[KEY_NOTIFICATION_ENABLED] = userSettings.isNotificationEnabled
        settings[KEY_SOUND_ENABLED] = userSettings.isSoundEnabled
        settings[KEY_VIBRATION_ENABLED] = userSettings.isVibrationEnabled
        settings[KEY_COMPACT_MODE] = userSettings.isCompactMode
        settings[KEY_MINIMAL_MODE] = userSettings.isMinimalMode
        settings[KEY_BATTERY_SAVER] = userSettings.isBatterySaverEnabled
        settings[KEY_HAS_COMPLETED_ONBOARDING] = userSettings.hasCompletedOnboarding
    }

    fun clear() {
        settings.clear()
    }

    fun getSettingsFlow(): Flow<UserSettings> {
        return kotlinx.coroutines.flow.flow {
            emit(getUserSettings())
        }
    }
}
