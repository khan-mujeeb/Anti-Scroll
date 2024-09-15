package com.example.antiscroll.data

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "available_app_setting")
data class AvailableAppSetting(
    @PrimaryKey(autoGenerate = true) val constraintId: Int = 0,
    val appName: String, // App name
    val upperTimeLimit: Long, // Time limit in milliseconds
    val isAntiScrollEnabled: Boolean // Is anti-scroll enabled for this app
)