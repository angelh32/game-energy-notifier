package com.example.energytimer

import com.example.energytimer.database.CustomTimer
import com.example.energytimer.tools.IncrementByTicTimer
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import java.util.*

class IncrementByTimerTest {
	private lateinit var timer: CustomTimer
	private lateinit var myTimer: IncrementByTicTimer
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

	private fun mockCallBack(time: Array<String>) {
		print(time)
		return
	}

	@Test
	fun create_new_timer_has_same_current() {
		myTimer = IncrementByTicTimer(timer) { timeLeft: Array<String> -> mockCallBack(timeLeft) }
		assertEquals("Expecting to find user John with id '0'", 20, myTimer.currentValue)
	}

	@Test
	fun create_existing_timer_has_correct_current() {
		val current = 20
		timer.startDate = currentDate - (current * 480 * 1000)
		myTimer = IncrementByTicTimer(timer) { timeLeft: Array<String> -> mockCallBack(timeLeft) }
		assertEquals(40, myTimer.currentValue)
	}

	@Test
	fun create_existing_timer_has_correct_next_tic() {
		timer.finishDate = timer.finishDate - (480 * 500)
		myTimer = IncrementByTicTimer(timer) { timeLeft: Array<String> -> mockCallBack(timeLeft) }
		assertEquals(239, myTimer.timeNextTic / 1000)
	}

	@Test
	fun create_existing_timer_has_correct_next_fist_value() {
		timer.finishDate = timer.finishDate - (480 * 500)
		myTimer = IncrementByTicTimer(timer) { timeLeft: Array<String> -> mockCallBack(timeLeft) }
		val resultArray = myTimer.formatAndReturn(60000)
		assertEquals("18:33:00", resultArray[0])
		assertEquals("01:00", resultArray[1])
		assertEquals("20/160", resultArray[2])
	}
}