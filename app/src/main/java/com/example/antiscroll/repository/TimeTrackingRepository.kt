package com.example.antiscroll.repository

import com.example.antiscroll.data.TimeTracking
import com.example.antiscroll.db.TimeTrackingDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class TimeTrackingRepository(private val timeTrackingDao: TimeTrackingDao) {

    // Insert time tracking data
    suspend fun insertTimeTracking(timeTracking: TimeTracking) {
        timeTrackingDao.insertTimeTracking(timeTracking)
    }

    // Get all tracking data ordered by date
    suspend fun getAllTrackingData(): List<TimeTracking> {
        return timeTrackingDao.getAllTrackingData()
    }

    // Get the total duration of usage for a specific app
    suspend fun getTotalDurationForApp(appName: String): Long {
        return timeTrackingDao.getTotalDurationForApp(appName)
    }
}
