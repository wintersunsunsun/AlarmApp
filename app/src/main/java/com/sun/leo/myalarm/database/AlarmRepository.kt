package com.sun.leo.myalarm.database

import androidx.lifecycle.LiveData

class AlarmRepository(private val alarmDao: AlarmDao) {

    // Room executes all queries on a separate thread.
    // Observed LiveData will notify the observer when the data has changed.
    val allAlarms: LiveData<List<Alarm>> = alarmDao.getAll()

    suspend fun insert(alarm: Alarm) {
        alarmDao.insert(alarm)
    }

    suspend fun delete(alarm: Alarm) {
        alarmDao.delete(alarm)
    }
}