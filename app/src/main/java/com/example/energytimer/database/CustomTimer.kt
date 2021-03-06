package com.example.energytimer.database

import androidx.room.*

@Entity(
	foreignKeys = [ForeignKey(
		entity = TimerType::class,
		parentColumns = arrayOf("typeId"),
		childColumns = arrayOf("typeId"),
		onDelete = ForeignKey.CASCADE
	)]
)
data class CustomTimer(
	@PrimaryKey(autoGenerate = true) var timerId: Int,
	@ColumnInfo var typeId: Int,
	@ColumnInfo var timerName: String,
	@ColumnInfo var description: String,
	@ColumnInfo var initial: Int,
	@ColumnInfo var max: Int,
	@ColumnInfo var tic: Int,
	@ColumnInfo var startDate: Long,
	@ColumnInfo var finishDate: Long,
)

data class TimerWithType(
	@Embedded val timer: CustomTimer,
	@Relation(
		parentColumn = "typeId",
		entityColumn = "typeId"
	)
	val type: TimerType
)

@Dao
interface CustomTimerDao {
	@Query("SELECT * FROM CustomTimer")
	fun getAll(): List<CustomTimer>

	@Query("SELECT * FROM CustomTimer WHERE timerId=:timerId LIMIT 1")
	fun findById(timerId: Int): CustomTimer

	@Query("UPDATE CustomTimer SET initial = :newInitial, startDate=:newStart, finishDate=:newFinish WHERE timerId =:id")
	fun update(id: Int, newInitial: Int, newStart: Long, newFinish: Long)


	@Transaction
	@Query("SELECT * FROM CustomTimer WHERE timerId=:timerId LIMIT 1")
	fun timerWithType(timerId: Int): TimerWithType

	@Query("DELETE FROM CustomTimer")
	fun deleteAll(): Int

	@Insert
	fun insertAll(vararg customTimers: CustomTimer)

	@Delete
	fun delete(customTimer: CustomTimer)
}

