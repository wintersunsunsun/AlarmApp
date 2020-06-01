package com.sun.leo.myalarm.page

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.sun.leo.myalarm.AlarmListAdapter
import com.sun.leo.myalarm.AlarmViewModel
import com.sun.leo.myalarm.R
import com.sun.leo.myalarm.database.Alarm
import com.sun.leo.myalarm.receiver.AlarmReceiver
import kotlinx.android.synthetic.main.activity_main.*
import java.util.*

const val CHANNEL_ID = "Alarm Channel"

class MainActivity : AppCompatActivity() {

    private val editActivityRequestCode = 1
    private lateinit var alarmViewModel: AlarmViewModel

    companion object {
        private const val TAG = "MainActivity"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setSupportActionBar(toolbar)

        val adapter = AlarmListAdapter(this)
        recyclerview.adapter = adapter
        recyclerview.layoutManager = LinearLayoutManager(this)

        alarmViewModel = ViewModelProvider(this).get(AlarmViewModel::class.java)
        alarmViewModel.allAlarms.observe(this, Observer { alarms ->
            alarms?.let { adapter.setAlarms(alarms) }
        })

        fab.setOnClickListener {
            Log.e(TAG, "Fab Click")
            startActivityForResult(
                Intent(this@MainActivity, EditActivity::class.java),
                editActivityRequestCode
            )
        }

        createNotificationChannel()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == editActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.run {
                val hour = getIntExtra(EditActivity.EXTRA_HOUR, 12)
                val minute = getIntExtra(EditActivity.EXTRA_MINUTE, 0)
                val alarm = Alarm(hour = hour, minute = minute)
                alarmViewModel.insert(alarm)
                addAlarm(alarm)
            }
        }
    }

    /**
     * 加入(與系統註冊)鬧鐘
     *
     */
    private fun addAlarm(alarm: Alarm) {
        val cal: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, alarm.hour)
            set(Calendar.MINUTE, alarm.minute)
            set(Calendar.SECOND, 0)
        }

        Log.e(
            TAG,
            "alarm add time: ${cal[Calendar.HOUR_OF_DAY]}:${cal[Calendar.MINUTE]}:${cal[Calendar.SECOND]}"
        )

        val intent = Intent(this, AlarmReceiver::class.java)
        // 以日期字串組出不同的 category 以添加多個鬧鐘
        intent.addCategory("ID.${cal[Calendar.HOUR_OF_DAY]}.${cal[Calendar.MINUTE]}.${cal[Calendar.SECOND]}")
        intent.putExtra("title", "activity_app")
        intent.putExtra(
            "time",
            "Alarmtime ${cal[Calendar.HOUR_OF_DAY]}:${cal[Calendar.MINUTE]}:${cal[Calendar.SECOND]}"
        )

        val alarmIntent =
            PendingIntent.getBroadcast(this, 1, intent, PendingIntent.FLAG_UPDATE_CURRENT)
        val am = getSystemService(Context.ALARM_SERVICE) as AlarmManager
        am.setRepeating(
            AlarmManager.RTC_WAKEUP,
            cal.timeInMillis,
            AlarmManager.INTERVAL_DAY,
            alarmIntent
        )
    }

    private fun createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                CHANNEL_ID,
                getString(R.string.channel_name),
                NotificationManager.IMPORTANCE_HIGH
            ).apply {
                description = getString(R.string.channel_description)
                enableVibration(true)
                enableLights(false)
            }
            // Register the channel with the system
            val notificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }
}
