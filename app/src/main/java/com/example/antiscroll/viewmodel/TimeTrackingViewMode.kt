package com.example.antiscroll.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antiscroll.data.TimeTracking
import com.example.antiscroll.repository.TimeTrackingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class TimeTrackingViewModel(private val repository: TimeTrackingRepository) : ViewModel() {

    // Insert a new TimeTracking record
    fun insertTimeTracking(timeTracking: TimeTracking) {
        viewModelScope.launch {
            withContext(Dispatchers.IO) {
                repository.insertTimeTracking(timeTracking)
            }
        }
    }

    // Get all time tracking records
    fun getAllTrackingData(onResult: (List<TimeTracking>) -> Unit) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.getAllTrackingData()
            }
            onResult(result)
        }
    }

    // Get total duration for a specific app
    fun getTotalDurationForApp(appName: String, onResult: (Long) -> Unit) {
        viewModelScope.launch {
            val result = withContext(Dispatchers.IO) {
                repository.getTotalDurationForApp(appName)
            }
            onResult(result)
        }
    }
}
