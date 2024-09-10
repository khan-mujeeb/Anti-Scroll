package com.example.antiscroll.uiUtils

import android.content.Context
import com.example.antiscroll.R
import com.example.antiscroll.adapter.NoScrollAppAdapter
import com.example.antiscroll.data.BlockScrollAppList
import com.example.antiscroll.databinding.ActivityMainBinding
import com.example.antiscroll.databinding.FragmentHomeBinding
import com.example.antiscroll.service.AccessibilityServiceHelper

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
    fun getAvilableAppList(context: Context, binding: FragmentHomeBinding) {

        binding.noScrollAppRc.adapter = NoScrollAppAdapter(context, BlockScrollAppList.appList)

    }




}