package com.sun.leo.myalarm.receiver

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import com.sun.leo.myalarm.page.MainActivity

class BootUpReceiver : BroadcastReceiver() {

    companion object {
        private const val TAG = "BootUpReceiver"
    }

    override fun onReceive(context: Context, intent: Intent) {
        if (intent.action.equals("android.intent.action.BOOT_COMPLETED")) {
            Log.e(TAG, "Boot Up!!!")
            intent.setClass(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK
            context.startActivity(intent)
        }
    }
}
