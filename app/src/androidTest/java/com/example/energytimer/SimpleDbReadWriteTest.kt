package com.example.energytimer

import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.energytimer.database.*
import com.example.energytimer.tools.Help.Companion.printLog
import junit.framework.Assert.assertEquals
import junit.runner.Version
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*


@RunWith(AndroidJUnit4::class)
class SimpleDbReadWriteTest {
	private var timerDao: CustomTimerDao
	private var typeDao: TimerTypeDao
	private var db: LocalDatabase
	private var customType: TimerType
	private var customTimer: CustomTimer

	init {
		val context = ApplicationProvider.getApplicationContext<Context>()
		db = Room.inMemoryDatabaseBuilder(context, LocalDatabase::class.java).build()
		customType =
			TimerType(1, "game","type-1", 160, 480)
		customTimer =
			CustomTimer(1, 1, "timer-1", "description", 1, 160, 480, Date().time, Date().time)
		timerDao = db.customTimerDao()
		typeDao = db.timerTypeDao()
		printLog("", "JUnit version is: " + Version.id())
	}

	@After
	@Throws(IOException::class)
	fun closeDb() {
		db.close()
	}

	@Test
	fun write_and_read_timer_type_in_list() {
		typeDao.insertAll(customType)
		val typeFromDb = typeDao.getAll()
		assertThat(typeFromDb[0], equalTo(customType))
	}

	@Test
	fun write_and_read_timer_type_without_timers_in_list() {
		typeDao.insertAll(customType)
		val typeFromDb = typeDao.typeWithTimers(customTimer.timerId)
		assertEquals(typeFromDb.timers.size, 0)
	}

	@Test
	fun write_non_existing_type_id_timer_in_list() {
		customTimer.timerId = 3
		customTimer.typeId = 3
		try {
			timerDao.insertAll(customTimer)
		} catch (exception: Exception) {
			assertEquals(SQLiteConstraintException::class, exception::class)
		}
	}

	@Test
	fun write_and_read_timer_in_list() {
		typeDao.insertAll(customType)
		timerDao.insertAll(customTimer)
		val byName = timerDao.getAll()
		assertThat(byName[0], equalTo(customTimer))
	}

	@Test
	fun write_and_read_a_timer_with_type() {
		customType.typeId = 4
		typeDao.insertAll(customType)
		customTimer.timerId = 4
		customTimer.typeId = 4
		timerDao.insertAll(customTimer)
		val typeFromDb = typeDao.typeWithTimers(customType.typeId)
		assertThat(typeFromDb.timers[0], equalTo(customTimer))
		assertThat(typeFromDb.type, equalTo(customType))
		val timerFromDb = timerDao.timerWithType(customTimer.timerId)
		assertThat(timerFromDb.timer, equalTo(customTimer))
		assertThat(timerFromDb.type, equalTo(customType))
	}
}
