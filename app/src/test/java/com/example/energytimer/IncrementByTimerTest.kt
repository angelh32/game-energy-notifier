package com.example.energytimer

import com.example.energytimer.database.CustomTimer
import com.example.energytimer.tools.IncrementByTimer
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*

class IncrementByTimerTest {
	private lateinit var timer: CustomTimer
	private lateinit var myTimer: IncrementByTimer
	private val currentDate = Date().time
	private val currentCount = 20
	private val maxValue = 160
	private val ticTime = 480

	@Before
	fun setScenario() {
		val finishDate = currentDate + ((maxValue - currentCount) * ticTime * 1000)
		timer = CustomTimer(
			1,
			1,
			"timer-1",
			"description",
			currentCount,
			160,
			480,
			currentDate,
			finishDate,
		)
	}

	@Test
	fun create_new_timer_has_same_current() {
		myTimer = IncrementByTimer(timer)
		assertEquals("Expecting to find user John with id '0'", 20, myTimer.currentValue)
	}

	@Test
	fun create_existing_timer_has_correct_current() {
		val current = 20
		timer.startDate = currentDate - (current * 480 * 1000)
		myTimer = IncrementByTimer(timer)
		assertEquals(40, myTimer.currentValue)
	}

	@Test
	fun create_existing_timer_has_correct_next_tic() {
		timer.finishDate = timer.finishDate - (480 * 500)
		myTimer = IncrementByTimer(timer)
		assertEquals(240, myTimer.timeNextTic / 1000)
	}
}