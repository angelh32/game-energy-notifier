package com.example.energytimer.tools

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent


class AlarmReceiver : BroadcastReceiver() {
	override fun onReceive(context: Context, intent: Intent) {
		val custom = CustomNotification(context)
		custom.raiseNotification("alarm", "notification from alarm")
	}
}
