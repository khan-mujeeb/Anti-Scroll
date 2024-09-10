package com.example.antiscroll.db



import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.antiscroll.data.TimeTracking

@Database(entities = [TimeTracking::class], version = 1)
abstract class TimeTrackingDatabase  : RoomDatabase() {
    abstract fun timeTrackingDao(): TimeTrackingDao

    companion object {

        @Volatile
        var INSTANCE: TimeTrackingDatabase? = null
        fun getDataBase(context: Context): TimeTrackingDatabase {
            val tempInstance = INSTANCE
            if (tempInstance != null)
                return tempInstance

            synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    TimeTrackingDatabase::class.java,
                    "time_tracking_database"
                ).build()
                INSTANCE = instance
                return instance
            }
        }
    }

}
