package com.example.energytimer.database

import androidx.room.*

@Entity
data class CustomTimer(
	@PrimaryKey(autoGenerate = true) val tid: Int,
	@ColumnInfo(name = "timer_name") val timerName: String,
	@ColumnInfo(name = "description") val description: String,
	@ColumnInfo(name = "initial") val initial: Int,
	@ColumnInfo(name = "max") val max: Int,
	@ColumnInfo(name = "tic") val tic: Int,
	@ColumnInfo(name = "start_date") var startDate: Long,
	@ColumnInfo(name = "finish_date") var finishDate: Long,
)

@Dao
interface CustomTimerDao {
	@Query("SELECT * FROM CustomTimer")
	fun getAll(): List<CustomTimer>

	@Query("SELECT * FROM CustomTimer WHERE tid IN (:timerIds)")
	fun loadAllByIds(timerIds: IntArray): List<CustomTimer>

	@Query("SELECT * FROM CustomTimer WHERE timer_name LIKE :timerName")
	fun findByName(timerName: String): CustomTimer

	@Insert
	fun insertAll(vararg customTimers: CustomTimer)

	@Delete
	fun delete(customTimer: CustomTimer)
}
