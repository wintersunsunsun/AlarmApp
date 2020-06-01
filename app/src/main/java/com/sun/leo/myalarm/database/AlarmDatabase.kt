package com.sun.leo.myalarm.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

@Database(entities = [Alarm::class], version = 1)
abstract class AlarmDatabase : RoomDatabase() {
    abstract fun alarmDao(): AlarmDao

    companion object {
        // Singleton prevents multiple instances of database opening at the
        // same time.
        @Volatile
        private var INSTANCE: AlarmDatabase? = null

        fun getDatabase(context: Context, scope: CoroutineScope): AlarmDatabase {
            // if the INSTANCE is not null, then return it,
            // if it is, then create the database
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AlarmDatabase::class.java,
                    "alarm_database"
                )
//                    .addCallback(AlarmDatabaseCallback(scope))
                    .build()
                INSTANCE = instance
                // return instance
                instance
            }
        }
    }

    private class AlarmDatabaseCallback(
        private val scope: CoroutineScope
    ) : RoomDatabase.Callback() {

        override fun onOpen(db: SupportSQLiteDatabase) {
            super.onOpen(db)
            INSTANCE?.let { database ->
                scope.launch {
                    populateDatabase(database.alarmDao())
                }
            }
        }


        suspend fun populateDatabase(alarmDao: AlarmDao) {
            // Delete all content here.
            alarmDao.deleteAll()

            // Add sample words.
            var alarm = Alarm(hour = 14, minute = 22)
            alarmDao.insert(alarm)
            alarm = Alarm(hour = 5, minute = 30)
            alarmDao.insert(alarm)
        }
    }
}