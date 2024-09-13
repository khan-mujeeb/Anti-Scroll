package com.example.antiscroll.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.antiscroll.data.TimeTracking

@Dao

interface TimeTrackingDao {
    @Insert
    suspend fun insertTimeTracking(timeTracking: TimeTracking)

//    @Query("SELECT * FROM time_tracking ORDER BY date DESC")
//    suspend fun getAllTrackingData(): List<TimeTracking>

    @Query("SELECT SUM(duration) FROM time_tracking WHERE appName = :appName")
    suspend fun getTotalDurationForApp(appName: String): Long

    @Query("SELECT SUM(duration) FROM time_tracking")
    fun getTotalDuration(): LiveData<Long>


}
