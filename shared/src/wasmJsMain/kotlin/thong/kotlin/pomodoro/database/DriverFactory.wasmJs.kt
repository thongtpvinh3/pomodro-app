package thong.kotlin.pomodoro.database

import app.cash.sqldelight.db.SqlDriver

actual fun createDriver(): SqlDriver {
    throw UnsupportedOperationException("SQLDelight not implemented on Wasm yet")
}
