package thong.kotlin.pomodoro.database

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver

actual fun createDriver(): SqlDriver {
    return NativeSqliteDriver(AuraDatabase.Schema, "aura.db")
}
