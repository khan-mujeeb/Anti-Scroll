package com.example.antiscroll.activity

import android.app.Application
import androidx.lifecycle.ViewModelProvider
import com.example.antiscroll.db.TimeTrackingDatabase.Companion.getDataBase
import com.example.antiscroll.repository.TimeTrackingRepository
import com.example.antiscroll.viewmodel.TimeTrackingViewModel
import com.example.antiscroll.viewmodel.TimeTrackingViewModelFactory


class MyApp : Application() {
    // Getter for ViewModel
    var timeTrackingViewModel: TimeTrackingViewModel? = null
        private set

    override fun onCreate() {
        super.onCreate()

        // Initialize the ViewModel using Application context and factory
        val database = getDataBase(this)
        val repository = TimeTrackingRepository(database.timeTrackingDao())
        val factory = TimeTrackingViewModelFactory(repository)

        // Use ViewModelProvider with the Application as the owner
        timeTrackingViewModel = ViewModelProvider.AndroidViewModelFactory(this).create<TimeTrackingViewModel>(
            TimeTrackingViewModel::class.java
        )
    }
}

