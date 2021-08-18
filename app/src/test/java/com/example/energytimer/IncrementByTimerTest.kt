package com.example.energytimer

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.example.energytimer.database.CustomTimer
import com.example.energytimer.tools.IncrementByTimer
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import java.util.*

class IncrementByTimerTest {
	@Rule
	@JvmField
	val instantTaskExecutorRule = InstantTaskExecutorRule()

	private lateinit var timer: CustomTimer
	private lateinit var myTimer: IncrementByTimer
	private val currentDate = Date().time
	private val currentCount = 20
	private val maxValue = 100
	private val ticTime = 100

	@Before
	fun setScenario() {
		val finishDate = currentDate + ((maxValue - currentCount) * ticTime * 1000)
		timer = CustomTimer(
			1,
			1,
			"timer-1",
			"description",
			currentCount,
			maxValue,
			ticTime,
			currentDate,
			finishDate,
		)
	}

	@Test
	fun create_new_timer_has_same_current() {
		myTimer = IncrementByTimer(timer)
		assertEquals(20, myTimer.currentValue)
	}

	@Test
	fun create_existing_timer_has_correct_current() {
		val current = 20
		timer.startDate = currentDate - (current * 480 * 1000)
		myTimer = IncrementByTimer(timer)
		assertEquals(116, myTimer.currentValue)
	}

	@Test
	fun create_existing_timer_has_correct_next_tic() {
		timer.finishDate = timer.finishDate - (480 * 500)
		myTimer = IncrementByTimer(timer)
		assertEquals(60, myTimer.timeNextTic / 1000)
	}

	@Test
	fun update_values_by_observer() {
		myTimer = IncrementByTimer(timer)
		var currentTimeLabel = ""
		var totalGeneratedLabel = ""
		var totalTimeLeftLabel = ""
		myTimer.currentTimeLabel.observeForever { value -> currentTimeLabel = value }
		myTimer.totalGeneratedLabel.observeForever { value -> totalGeneratedLabel = value }
		myTimer.totalTimeLeftLabel.observeForever { value -> totalTimeLeftLabel = value }
		myTimer.updateLiveValues(1000)
		assertEquals("00:01", currentTimeLabel)
		assertEquals("20/100", totalGeneratedLabel)
		assertEquals("02:13:21", totalTimeLeftLabel)
	}
}