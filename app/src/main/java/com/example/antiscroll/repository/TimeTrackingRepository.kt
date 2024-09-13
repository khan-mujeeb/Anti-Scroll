package com.example.antiscroll.repository

import androidx.lifecycle.LiveData
import com.example.antiscroll.data.TimeTracking
import com.example.antiscroll.db.TimeTrackingDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TimeTrackingRepository(private val timeTrackingDao: TimeTrackingDao) {

    //************** Insert time tracking data *****************
    suspend fun insertTimeTracking(timeTracking: TimeTracking) {
        timeTrackingDao.insertTimeTracking(timeTracking)
    }


    // ******************* Get the total duration of usage for a specific app *******************
    suspend fun getTotalDurationForApp(appName: String): Long {
        return timeTrackingDao.getTotalDurationForApp(appName)
    }


    // ***************** get the total duration of usage for all apps *********************
    fun getTotalDuration(): LiveData<Long> {
        return timeTrackingDao.getTotalDuration()
    }
}
