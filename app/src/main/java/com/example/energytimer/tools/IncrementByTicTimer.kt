package com.example.energytimer.tools

import android.os.CountDownTimer
import com.example.energytimer.database.CustomTimer
import java.util.*

class IncrementByTicTimer(
	currentTimer: CustomTimer,
	private val updateTextCallback: (timeLeft: Array<String>) -> Unit,
) {
	private lateinit var cTimer: CountDownTimer
	var isTimerRunning = false
	private var timer: CustomTimer = currentTimer
	var currentValue = 0 // in energy units
	var timeNextTic: Long = 0 // in milliseconds
	private var timeToFinish: Long = 0 // in seconds

	init {
		refreshValues()
	}

	private fun refreshValues(){
		val currentDate = Date().time
		val stack = (currentDate-timer.startDate)/(timer.tic*1000)
		currentValue=timer.initial+stack.toInt()
		val timeLeft = timer.finishDate-currentDate
		timeNextTic = timeLeft.mod(timer.tic*1000).toLong()
		timeToFinish=timeLeft-timeNextTic
	}

	fun startCount() {
		if (currentValue < timer.max) {
			refreshValues()
			cancelTimer()
			cTimer.start()
		}
	}

	fun formatAndReturn(timeLeft: Long): Array<String>{
		return arrayOf(
			formatFromMilliseconds(timeLeft+timeToFinish),
			formatFromMilliseconds(timeLeft),
			"$currentValue/${timer.max}"
		)
	}

	private fun formatFromMilliseconds(timeLeft: Long):String{
		val valuesArray: Array<Long> = arrayOf(
			timeLeft / (24 * 60 * 60 * 1000), // days
			timeLeft / (60 * 60 * 1000) % 24, // hours
			timeLeft / (60 * 1000) % 60, // minutes
			timeLeft / 1000 % 60, // seconds
		)
		var response=""
		for (i in valuesArray) {
			if (i > 0 || response != "") {
				response += String.format("%02d:", i)
			}
		}
		if(response.isNotEmpty()) {
			response = response.substring(0, response.length - 1)
		}
		return response
	}

	fun startTimer() {
		cTimer = object: CountDownTimer(timeNextTic, 1000) {
			override fun onTick(timeLeft: Long) {
				updateTextCallback(formatAndReturn(timeLeft))
			}
			override fun onFinish() {
				currentValue++
				startCount()
			}
		}
		isTimerRunning=true
		cTimer.start()
	}

	fun cancelTimer() {
		cTimer.cancel()
	}
}