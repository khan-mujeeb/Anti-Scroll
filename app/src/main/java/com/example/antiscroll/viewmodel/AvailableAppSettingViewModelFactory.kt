package com.example.antiscroll.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.antiscroll.repository.AvailableAppSettingRepository

class AvailableAppSettingViewModelFactory(
    private val repository: AvailableAppSettingRepository
): ViewModelProvider.Factory {

        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(AvailableAppSettingViewModel::class.java)) {
                return AvailableAppSettingViewModel(repository) as T
            }
            throw IllegalArgumentException("Unknown ViewModel class")
        }
}