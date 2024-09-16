package com.example.antiscroll.uiUtils

import android.content.Context
import androidx.lifecycle.LifecycleOwner
import com.example.antiscroll.adapter.NoScrollAppAdapter
import com.example.antiscroll.data.AvailableAppSetting
import com.example.antiscroll.data.BlockScrollAppList
import com.example.antiscroll.databinding.FragmentHomeBinding
import com.example.antiscroll.service.AccessibilityServiceHelper
import com.example.antiscroll.viewmodel.AvailableAppSettingViewModel

object UIUpdater {

    //    ***** function to update the service status *****
    fun updateServiceStatus(context: Context, binding: FragmentHomeBinding) {
        if (AccessibilityServiceHelper.isServiceEnabled(context)) {
            binding.enableServiceButton.isEnabled = false
            binding.disbleServiceButton.isEnabled = true
        } else {
            binding.disbleServiceButton.isEnabled = false
            binding.enableServiceButton.isEnabled = true
        }
    }


    //    ***** function to retrieve the available app list that are support by anti scroll app*****
    fun getAvilableAppList(
        context: Context,
        binding: FragmentHomeBinding,
        availableAppViewModel: AvailableAppSettingViewModel,
        viewLifecycleOwner: LifecycleOwner,
        availableAppList: List<AvailableAppSetting>
    ) {

        binding.noScrollAppRc.adapter = NoScrollAppAdapter(
            context = context,
            appList = BlockScrollAppList.appList,
            availableAppViewModel = availableAppViewModel,
            lifecycleOwner = viewLifecycleOwner,
            availableAppList = availableAppList
        )

    }




}