package com.example.energytimer.database

import androidx.room.*

@Entity
data class TimerType(
	@PrimaryKey(autoGenerate = true) var typeId: Int,
	@ColumnInfo() var gameName: String,
	@ColumnInfo() var typeName: String,
	@ColumnInfo() var description: String,
	@ColumnInfo() var max: Int,
	@ColumnInfo() var tic: Int,
)

data class TimerTypeWithCustomTimers(
	@Embedded val type: TimerType,
	@Relation(
		parentColumn = "typeId",
		entityColumn = "typeId"
	)
	val timers: List<CustomTimer>
)

@Dao
interface TimerTypeDao {
	@Query("SELECT * FROM TimerType")
	fun getAll(): List<TimerType>

	@Query("SELECT * FROM TimerType WHERE typeId=:id LIMIT 1")
	fun findById(id: Int): TimerType

	@Transaction
	@Query("SELECT * FROM TimerType WHERE typeId=:id LIMIT 1")
	fun typeWithTimers(id: Int): TimerTypeWithCustomTimers

	@Query("DELETE FROM TimerType")
	fun deleteAll(): Int

	@Insert
	fun insertAll(vararg customTimers: TimerType)

	@Delete
	fun delete(customTimer: TimerType)
}
