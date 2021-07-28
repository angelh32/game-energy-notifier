package com.example.energytimer.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CustomTimer::class, TimerType::class], version = 1)
abstract class LocalDatabase : RoomDatabase() {
	abstract fun customTimerDao(): CustomTimerDao
	abstract fun timerTypeDao(): TimerTypeDao
}