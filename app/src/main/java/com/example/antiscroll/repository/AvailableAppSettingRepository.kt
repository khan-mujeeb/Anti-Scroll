package com.example.antiscroll.repository

import com.example.antiscroll.data.AvailableAppSetting
import com.example.antiscroll.db.AvailableAppSettingDao
import kotlinx.coroutines.flow.Flow

class AvailableAppSettingRepository(private val availableAppSettingDao: AvailableAppSettingDao) {


    // *************** insert default settings ***************
    suspend fun insertDefaultSettings(defaultSetting: List<AvailableAppSetting>) {
        availableAppSettingDao.insertDefaultSettings(defaultSetting)
    }


    // *************** get app specific setting by package name ***************
    fun getAvailableAppSetting(packageName: String): Flow<AvailableAppSetting?> {
        return availableAppSettingDao.getAvailableAppSetting(packageName)
    }

    // *************** update app setting ***************
    suspend fun updateAppSetting(packageName: String, isBlocked: Boolean) {
        availableAppSettingDao.updateAppSetting(packageName, isBlocked)
    }

    // *************** get isAntiScrollEnabled ***************
    suspend fun getIsAntiScrollEnabled(packageName: String): Int {


        return availableAppSettingDao.getIsAntiScrollEnabled(packageName)
    }

    // *************** get all settings ***************
    fun getAllSettings(): Flow<List<AvailableAppSetting>>{
        return availableAppSettingDao.getAllSettings()
    }


    // *************** update time limit ***************
    suspend fun updateTimeLimit(packageName: String, time: Long) {
        availableAppSettingDao.updateTimeLimit(packageName, time)
    }

}
