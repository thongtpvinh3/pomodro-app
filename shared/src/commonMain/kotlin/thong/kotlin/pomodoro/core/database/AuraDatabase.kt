package thong.kotlin.pomodoro.core.database

import thong.kotlin.pomodoro.core.database.dao.TaskDao

abstract class AuraDatabase {
    abstract fun taskDao(): TaskDao
}