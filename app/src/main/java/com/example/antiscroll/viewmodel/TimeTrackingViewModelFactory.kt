package com.example.antiscroll.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.antiscroll.repository.TimeTrackingRepository

class TimeTrackingViewModelFactory(
    private val repository: TimeTrackingRepository
) : ViewModelProvider.Factory {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(TimeTrackingViewModel::class.java)) {
            return TimeTrackingViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
