package thong.kotlin.pomodoro.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.jdbc.sqlite.JdbcSqliteDriver
import java.io.File
import java.util.Properties

actual fun createDriver(): SqlDriver {
    val databasePath = File(System.getProperty("user.home"), ".aura_pomodoro/aura.db")
    if (!databasePath.parentFile.exists()) {
        databasePath.parentFile.mkdirs()
    }
    
    val driver: SqlDriver = JdbcSqliteDriver("jdbc:sqlite:${databasePath.absolutePath}", Properties())
    try {
        AuraDatabase.Schema.create(driver)
    } catch (e: Exception) {
        // Table already exists or other error
    }
    return driver
}
