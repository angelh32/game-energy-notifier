package com.example.energytimer.database

import androidx.room.*

@Entity
data class TimerType(
	@PrimaryKey(autoGenerate = true) val tid: Int,
	@ColumnInfo(name = "timer_type_name") val timerTypeName: String,
	@ColumnInfo(name = "max") val max: Int,
	@ColumnInfo(name = "tic") val tic: Int,
)

@Dao
interface TimerTypeDao {
	@Query("SELECT * FROM TimerType")
	fun getAll(): List<TimerType>

	@Query("SELECT * FROM TimerType WHERE tid IN (:timerIds)")
	fun loadAllByIds(timerIds: IntArray): List<TimerType>

	@Query("SELECT * FROM TimerType WHERE timer_type_name LIKE :timerTypeName")
	fun findByName(timerTypeName: String): TimerType

	@Insert
	fun insertAll(vararg customTimers: TimerType)

	@Delete
	fun delete(customTimer: TimerType)
}
