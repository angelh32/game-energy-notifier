package com.example.energytimer.tools

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import com.example.energytimer.R
import kotlin.random.Random

class CustomNotification(newContext: Context) {
	private var context: Context = newContext

	init {
		createNotificationChannel()
	}

	fun generateId(): Int {
		return Random.nextInt(0, 10000)
	}

	fun raiseNotification(title: String, body: String) {
		var builder = NotificationCompat.Builder(context, ChannelId)
			.setSmallIcon(R.drawable.ic_add)
			.setContentTitle(title)
			.setContentText(body)
			.setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
			.setPriority(NotificationCompat.PRIORITY_DEFAULT)

		val notifications: NotificationManager =
			context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager;
		notifications.notify(generateId(), builder.build())
	}

	private fun createNotificationChannel() {
		// Create the NotificationChannel, but only on API 26+ because
		// the NotificationChannel class is new and not in the support library
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			val importance = NotificationManager.IMPORTANCE_DEFAULT
			val channel = NotificationChannel(ChannelId, ChannelName, importance).apply {
				description = ChannelDescription
			}
			// Register the channel with the system
			val notificationManager: NotificationManager =
				context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
			notificationManager.createNotificationChannel(channel)
		}
	}
}