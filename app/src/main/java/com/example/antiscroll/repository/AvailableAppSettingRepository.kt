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

}
