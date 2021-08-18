package com.example.energytimer.tools

import android.os.CountDownTimer
import androidx.lifecycle.MutableLiveData
import com.example.energytimer.database.CustomTimer
import com.example.energytimer.tools.Help.Companion.formatFromMilliseconds
import java.util.*

class IncrementByTimer(currentTimer: CustomTimer) {
	val totalTimeLeftLabel = MutableLiveData<String>()
	val totalGeneratedLabel = MutableLiveData<String>()
	val currentTimeLabel = MutableLiveData<String>()

	private lateinit var cTimer: CountDownTimer
	var isTimerRunning = false
	private var timer: CustomTimer = currentTimer
	var currentValue = 0 // in energy units
	var timeNextTic: Long = 0 // in milliseconds
	private var timeToFinish: Long = 0 // in seconds

	init {
		refreshValues()
	}

	private fun refreshValues() {
		val currentDate = Date().time
		val stack = (currentDate - timer.startDate) / (timer.tic * 1000)
		currentValue = timer.max
		if (timer.finishDate < currentDate) {
			totalTimeLeftLabel.value = "Completed"
			totalGeneratedLabel.value = String.format("%02d / %02d", currentValue, currentValue)
			currentTimeLabel.value = "-- : --"
		} else {
			currentValue = timer.initial + stack.toInt()
			val timeLeft = timer.finishDate - currentDate
			timeNextTic = timeLeft.mod(timer.tic * 1000).toLong()
			timeToFinish = timeLeft - timeNextTic
		}
	}

	fun startCount() {
		if (currentValue < timer.max) {
			refreshValues()
			cancelTimer()
			cTimer.start()
		}
	}

	fun startTimer() {
		cTimer = object : CountDownTimer(timeNextTic, 1000) {
			override fun onTick(timeLeft: Long) {
				totalTimeLeftLabel.value = formatFromMilliseconds(timeLeft + timeToFinish)
				currentTimeLabel.value = formatFromMilliseconds(timeLeft)
				totalGeneratedLabel.value = "$currentValue/${timer.max}"
			}

			override fun onFinish() {
				currentValue++
				startCount()
			}
		}
		isTimerRunning = true
		cTimer.start()
	}

	fun cancelTimer() {
		cTimer.cancel()
	}
}