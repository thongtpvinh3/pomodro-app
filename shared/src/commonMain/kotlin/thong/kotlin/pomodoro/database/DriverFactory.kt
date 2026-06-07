package thong.kotlin.pomodoro.database

import app.cash.sqldelight.db.SqlDriver

expect fun createDriver(): SqlDriver
