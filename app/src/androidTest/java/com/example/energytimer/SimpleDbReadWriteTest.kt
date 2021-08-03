package com.example.energytimer

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.energytimer.database.*
import com.example.energytimer.tools.Help
import org.hamcrest.CoreMatchers.equalTo
import org.hamcrest.MatcherAssert.assertThat
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.util.*

@RunWith(AndroidJUnit4::class)
class SimpleDbReadWriteTest {
	private lateinit var timerDao: CustomTimerDao
	private lateinit var typeDao: TimerTypeDao
	private lateinit var db: LocalDatabase

	@Before
	fun createDb() {
		val context = ApplicationProvider.getApplicationContext<Context>()
		db = Room.inMemoryDatabaseBuilder(context, LocalDatabase::class.java).build()
		timerDao = db.customTimerDao()
		typeDao = db.timerTypeDao()
		timerDao.getAll()
	}

	@After
	@Throws(IOException::class)
	fun closeDb() {
		db.close()
	}

	@Test
	fun writeAndReadTimerTypeInList() {
		val customType =
			TimerType(1, "timer-1",  160, 480)
		typeDao.insertAll(customType)
		val byName = typeDao.getAll()
		assertThat(byName[0], equalTo(customType))
	}

	@Test
	fun writeAndReadTimerInList() {
		val customTimer =
			CustomTimer(1, "timer-1", "description", 1, 160, 480, Date().time, Date().time)
		timerDao.insertAll(customTimer)
		Help.printLog("Test", customTimer.toString())
		val byName = timerDao.getAll()
		assertThat(byName[0], equalTo(customTimer))
	}
}
