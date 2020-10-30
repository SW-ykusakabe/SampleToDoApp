package com.example.todoapp.models

import android.R
import android.app.Notification
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.todoapp.controller.MainActivity


class AlarmBroadcastReceiver: BroadcastReceiver() {
    var NOTIFICATION_ID = "notificationId"
    var NOTIFICATION_CONTENT = "NOTIFICATION_CHANNEL_ID_SAMPLE"

    override fun onReceive(context: Context, intent: Intent) {
        val content = intent.getStringExtra(NOTIFICATION_CONTENT)
        val notifyIntent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(context, 0, notifyIntent, 0)

        val channelId = "NOTIFICATION_CHANNEL_ID_SAMPLE"
        val builder = NotificationCompat.Builder(context, channelId).apply {
            setSmallIcon(com.example.todoapp.R.drawable.ic_launcher_foreground)
            setContentTitle("Notification Title")
            setContentText("のーてぃふぃけーしょん　てきすと")
            priority = NotificationCompat.PRIORITY_DEFAULT

            setContentIntent(pendingIntent)
            setAutoCancel(true)
        }
        with(NotificationManagerCompat.from(context)) {
            Log.d("tag", "test")
            notify(1, builder.build())
        }
    }
}