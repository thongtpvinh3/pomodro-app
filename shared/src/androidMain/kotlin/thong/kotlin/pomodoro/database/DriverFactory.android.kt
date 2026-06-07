package thong.kotlin.pomodoro.database

import android.content.Context
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver

private var androidContext: Context? = null

fun initDatabaseContext(context: Context) {
    androidContext = context
}

actual fun createDriver(): SqlDriver {
    val context = androidContext ?: throw IllegalStateException("Android context not initialized. Call initDatabaseContext first.")
    return AndroidSqliteDriver(AuraDatabase.Schema, context, "aura.db")
}
