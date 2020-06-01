package com.sun.leo.myalarm.page

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.sun.leo.myalarm.R
import kotlinx.android.synthetic.main.activity_alert.*

class AlertActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_alert)
        setSupportActionBar(toolbar)

        supportActionBar?.run {
            setHomeAsUpIndicator(R.drawable.ic_close)
            setDisplayHomeAsUpEnabled(true)
            setDisplayShowTitleEnabled(false)
        }

        toolbar.setNavigationOnClickListener {
            onBackPressed()
        }

        text.text = intent.getStringExtra("time")
    }
}
