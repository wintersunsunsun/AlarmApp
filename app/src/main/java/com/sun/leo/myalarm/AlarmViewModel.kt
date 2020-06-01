package com.sun.leo.myalarm

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.sun.leo.myalarm.database.Alarm
import com.sun.leo.myalarm.database.AlarmDatabase
import com.sun.leo.myalarm.database.AlarmRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmViewModel(application: Application) : AndroidViewModel(application) {

    private val repository: AlarmRepository
    val allAlarms: LiveData<List<Alarm>>

    init {
        val alarmsDao = AlarmDatabase.getDatabase(application, viewModelScope).alarmDao()
        repository = AlarmRepository(alarmsDao)
        allAlarms = repository.allAlarms
    }

    /**
     * Launching a new coroutine to insert the data in a non-blocking way
     */
    fun insert(alarm: Alarm) = viewModelScope.launch(Dispatchers.IO) {
        repository.insert(alarm)
    }

    fun delete(alarm: Alarm) = viewModelScope.launch(Dispatchers.IO) {
        repository.delete(alarm)
    }
}