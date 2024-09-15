package com.example.antiscroll.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.antiscroll.data.AvailableAppSetting
import com.example.antiscroll.repository.AvailableAppSettingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class AvailableAppSettingViewModel(private val repository: AvailableAppSettingRepository): ViewModel() {


    // ********** insert default settings **********

    suspend fun insertDefaultSettings(defaultSetting: List<AvailableAppSetting>) {

        viewModelScope.launch {
            withContext(Dispatchers.IO){
                repository.insertDefaultSettings(defaultSetting)
            }
        }
    }

    // ************** update app setting **************
    suspend fun updateAppSetting(packageName: String, isBlocked: Boolean) {


            withContext(Dispatchers.IO) {
                repository.updateAppSetting(packageName, isBlocked)
            }


    }

    // ********** get app specific setting by package name **********
    fun getAvailableAppSetting(packageName: String): Flow<AvailableAppSetting?> {
        return repository.getAvailableAppSetting(packageName)
    }

}
