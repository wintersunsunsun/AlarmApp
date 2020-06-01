package com.sun.leo.myalarm.receiver

import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import com.sun.leo.myalarm.receiver.AlarmReceiver.Companion.NOTIFICATION_ID

class NotificationReceiver : BroadcastReceiver() {

    companion object {
        const val ACTION_CLOSE_NOTIFICATION =
            "com.sun.leo.myalarm.ACTION_CLOSE_NOTIFICATION"
    }

    override fun onReceive(context: Context, intent: Intent) {

        // Cancel the notification.
        (context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager).cancel(
            NOTIFICATION_ID
        )
    }
}
