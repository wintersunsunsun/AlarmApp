package com.sun.leo.myalarm.page

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.sun.leo.myalarm.R
import kotlinx.android.synthetic.main.activity_edit.*

class EditActivity : AppCompatActivity() {

    companion object {
        const val EXTRA_HOUR = "com.sun.leo.alarm.HOUR"
        const val EXTRA_MINUTE = "com.sun.leo.alarm.MINUTE"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit)
        setSupportActionBar(toolbar)

        supportActionBar?.run {
            setHomeAsUpIndicator(R.drawable.ic_close)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        confirm_button.setOnClickListener {
            val replyIntent = Intent()

            replyIntent.putExtra(EXTRA_HOUR, time_picker.hour)
            replyIntent.putExtra(EXTRA_MINUTE, time_picker.minute)
            setResult(Activity.RESULT_OK, replyIntent)
            finish()
        }
    }
}
