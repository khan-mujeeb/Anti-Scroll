package com.example.antiscroll.worker

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.antiscroll.db.TimeTrackingDatabase

class ClearDatabaseWorker(
    context: Context,

    workerParams: WorkerParameters
) : CoroutineWorker(context, workerParams) {

    override suspend fun doWork(): Result {
        return try {
            val database = TimeTrackingDatabase.getDataBase(applicationContext) // Replace with your database instance
            database.timeTrackingDao().clearAllData() // Ensure you have a DAO method to delete all rows
            Result.success()
        } catch (e: Exception) {
            e.printStackTrace()
            Result.failure()
        }
    }
}
