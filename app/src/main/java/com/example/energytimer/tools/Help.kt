package com.example.energytimer.tools

import android.util.Log

class Help {
	companion object {
		fun printLog(origin: String, message: String) {
			Log.i("APX-$origin", message)
		}

		fun formatFromSeconds(timeLeft: Long): String {
			return formatFromMilliseconds(timeLeft * 1000)
		}

		fun parseMillisecondsFromString(time: String): Long {
			val parsed = time.split(":")
			when (parsed.size) {
				2 -> {
					val minutes = parsed[0].toLong() * (60 * 1000)
					val seconds = parsed[1].toLong() * 1000
					return minutes + seconds
				}
				3 -> {
					val hours = parsed[0].toLong() * (60 * 60 * 1000)
					val minutes = parsed[1].toLong() * (60 * 1000)
					val seconds = parsed[2].toLong() * 1000
					return hours + minutes + seconds
				}
				else -> {
					val days = parsed[0].toLong() * (24 * 60 * 60 * 1000)
					val hours = parsed[1].toLong() * (60 * 60 * 1000)
					val minutes = parsed[2].toLong() * (60 * 1000)
					val seconds = parsed[3].toLong() * 1000
					return days + hours + minutes + seconds
				}
			}
		}

		fun formatFromMilliseconds(timeLeft: Long): String {
			val days = timeLeft / (24 * 60 * 60 * 1000)
			val hours = timeLeft / (60 * 60 * 1000) % 24
			val minutes = timeLeft / (60 * 1000) % 60
			val seconds = timeLeft / 1000 % 60
			val response = String.format("%02d:%02d:%02d:%02d", days, hours, minutes, seconds)
			return when {
				days.toInt() == 0 && hours.toInt() == 0 -> response.replaceFirst("00:00:", "")
				days.toInt() == 0 -> response.replaceFirst("00:", "")
				else -> response
			}
		}
	}
}