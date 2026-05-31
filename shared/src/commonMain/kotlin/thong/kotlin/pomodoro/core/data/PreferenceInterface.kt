package thong.kotlin.pomodoro.core.data

import kotlinx.coroutines.delay

interface AppPreferences {
    suspend fun getWorkDurationMinutes(): Int
    suspend fun isSoundEnabled(): Boolean
    suspend fun markTutorialAsSeen()
    suspend fun getSelectedBackgroundId()
    suspend fun getUnlockedBackgroundIds() : Set<String>
    suspend fun saveSelectedBackgroundId(id: String)
    suspend fun saveUnlockedBackgroundIds(ids: Set<String>)
}