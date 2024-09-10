package com.example.antiscroll.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "time_tracking")
data class TimeTracking(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val appName: String,
    val duration: Long, // Duration in milliseconds
    val day: Long, // Timestamp when tracked
    val timeLimit : Long // Time limit in milliseconds
)
