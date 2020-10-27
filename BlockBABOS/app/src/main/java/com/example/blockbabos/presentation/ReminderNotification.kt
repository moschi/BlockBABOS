package com.example.blockbabos.presentation

import android.app.NotificationManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.core.app.NotificationCompat


class ReminderNotification: BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        Log.d(CHANNEL_ID, "ReminderNotification started")
        val contentIntent = PendingIntent.getActivity(
            context, 0,
            Intent(context, ReminderNotification::class.java), 0

        )
        val text = intent?.getStringExtra("text")

        if (context != null) {
            val builder = NotificationCompat.Builder(context, CHANNEL_ID)
                .setSmallIcon(com.example.blockbabos.R.drawable.logo)
                .setContentTitle("BlockBABO")
                .setContentText(text)

                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setContentIntent(contentIntent)
                .setAutoCancel(true)

            val mNotificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            mNotificationManager.notify(1, builder.build())
        }
    }
}