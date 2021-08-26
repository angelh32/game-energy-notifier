package com.example.energytimer.tools

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.SystemClock

class CustomAlarm(newContext: Context) {
	private var context: Context = newContext
	private lateinit var alarmMgr: AlarmManager
	private lateinit var alarmIntent: PendingIntent

	init {
		buildAlarm()
	}

	private fun buildAlarm() {
		alarmMgr = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
		alarmIntent = Intent(context, AlarmReceiver::class.java).let { intent ->
			PendingIntent.getBroadcast(context, 0, intent, 0)
		}
	}

	fun setAlarm() {
		alarmMgr.set(
			AlarmManager.ELAPSED_REALTIME_WAKEUP,
			SystemClock.elapsedRealtime() + 60 * 1000,
			alarmIntent
		)

	}
}