package com.example.energytimer

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.energytimer.database.LocalDatabase
import com.example.energytimer.database.CustomTimer
import com.example.energytimer.database.CustomTimerDao
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
	private lateinit var db: LocalDatabase

	@Before
	fun createDb() {
		val context = ApplicationProvider.getApplicationContext<Context>()
		db = Room.inMemoryDatabaseBuilder(context, LocalDatabase::class.java).build()
		timerDao = db.customTimerDao()
		timerDao.getAll()
	}

	@After
	@Throws(IOException::class)
	fun closeDb() {
		db.close()
	}

	@Test
	@Throws(Exception::class)
	fun writeUserAndReadInList() {
		val customTimer =
			CustomTimer(1, "timer-1", "description", 1, 160, 480, Date().time, Date().time)
		timerDao.insertAll(customTimer)
		val byName = timerDao.getAll()
		assertThat(byName[0], equalTo(customTimer))
	}
}
