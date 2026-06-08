package thong.kotlin.pomodoro.features.pomodoro.data.local

import thong.kotlin.pomodoro.database.AuraDatabase
import thong.kotlin.pomodoro.database.createDriver
import app.cash.sqldelight.coroutines.asFlow
import app.cash.sqldelight.coroutines.mapToList
import app.cash.sqldelight.coroutines.mapToOneOrNull
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import thong.kotlin.pomodoro.database.TaskRecord
import thong.kotlin.pomodoro.database.SessionRecord
import thong.kotlin.pomodoro.database.DailyStats

class LocalDatabaseDataSource {
    private val database = AuraDatabase(createDriver())
    private val queries = database.auraDatabaseQueries

    fun getAllTasks(): Flow<List<TaskRecord>> {
        return queries.selectAllTasks()
            .asFlow()
            .mapToList(Dispatchers.Default)
    }

    suspend fun insertTask(record: TaskRecord) {
        queries.insertTask(record)
    }

    suspend fun deleteTask(id: String) {
        queries.deleteTask(id)
    }

    fun getAllSessions(): Flow<List<SessionRecord>> {
        return queries.selectAllSessions()
            .asFlow()
            .mapToList(Dispatchers.Default)
    }

    suspend fun insertSession(record: SessionRecord) {
        queries.insertSession(record)
    }

    fun getStatsByDate(date: String): Flow<DailyStats?> {
        return queries.getStatsByDate(date)
            .asFlow()
            .mapToOneOrNull(Dispatchers.Default)
    }

    suspend fun insertOrUpdateStats(record: DailyStats) {
        queries.insertOrUpdateStats(record)
    }

    suspend fun clearAllData() {
        database.transaction {
            queries.deleteAllTasks()
            queries.deleteAllSessions()
            queries.deleteAllStats()
        }
    }
}
