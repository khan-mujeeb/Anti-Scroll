package com.example.antiscroll.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.antiscroll.data.AvailableAppSetting
import kotlinx.coroutines.flow.Flow

@Dao
interface AvailableAppSettingDao {


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDefaultSettings(defaultSetting: List<AvailableAppSetting>)

    @Query("SELECT * FROM available_app_setting")
    fun getAllSettings(): List<AvailableAppSetting>

    @Query("SELECT * FROM available_app_setting WHERE appName = :packageName")
    fun getAvailableAppSetting(packageName: String): Flow<AvailableAppSetting?>


    @Query("Update available_app_setting SET isAntiScrollEnabled = :isBlocked WHERE appName = :packageName")
    suspend fun updateAppSetting(packageName: String, isBlocked: Boolean)
}