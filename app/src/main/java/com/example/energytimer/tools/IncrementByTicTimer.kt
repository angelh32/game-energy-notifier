package com.example.energytimer.tools

import android.os.CountDownTimer
import com.example.energytimer.database.CustomTimer
import java.util.*

class IncrementByTicTimer(
	currentTimer: CustomTimer,
	private val updateTextCallback: (timeLeft: Array<String>) -> Unit,
) {
	private lateinit var cTimer: CountDownTimer
	var isTimerRuning = false
	private var timer: CustomTimer = currentTimer
	var currentValue = 0 // in energy units
	var timeNextTic: Long = 0 // in milliseconds
	var timeToFinish: Long = 0 // in seconds

	init {
		refreshValues()
	}

	fun refreshValues(){
		val currentDate = Date().time
		val stack = (currentDate-timer.startDate)/(timer.tic*1000)
		currentValue=timer.initial+stack.toInt()
		val timeLeft = timer.finishDate-currentDate
		timeNextTic = timeLeft.mod(timer.tic*1000).toLong()
		timeToFinish=timeLeft-timeNextTic
	}

	fun startCount() {
		if (currentValue < timer.max) {
			startTimer()
		}
	}

	fun formatAndReturn(timeLeft: Long): Array<String>{
		return arrayOf(
			formatFromMillisecons(timeLeft+timeToFinish),
			formatFromMillisecons(timeLeft),
			"$currentValue/${timer.max}"
		)
	}

	fun formatFromMillisecons(timeLeft: Long):String{
		var valuesArray = arrayOf(
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
		if(response.length>0) {
			response = response.substring(0, response.length - 1);
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
				if (currentValue<timer.max){
					refreshValues()
					cancelTimer()
					cTimer.start()
				}
			}
		}
		isTimerRuning=true
		cTimer.start()
	}

	fun cancelTimer() {
		cTimer!!.cancel()
	}
}