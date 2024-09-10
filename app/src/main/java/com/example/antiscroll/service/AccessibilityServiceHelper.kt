package com.example.antiscroll.service

import android.content.Context
import android.provider.Settings
import android.text.TextUtils

object AccessibilityServiceHelper {

    fun isServiceEnabled(context: Context): Boolean {
        val service = "${context.packageName}/${MyAccessibilityService::class.java.canonicalName}"
        return try {
            val enabled = Settings.Secure.getInt(context.contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED)
            if (enabled == 1) {
                val settingValue = Settings.Secure.getString(context.contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
                if (settingValue != null) {
                    val colonSplitter = TextUtils.SimpleStringSplitter(':')
                    colonSplitter.setString(settingValue)
                    while (colonSplitter.hasNext()) {
                        val componentName = colonSplitter.next()
                        if (componentName.equals(service, ignoreCase = true)) {
                            return true
                        }
                    }
                }
            }
            false
        } catch (e: Settings.SettingNotFoundException) {
            e.printStackTrace()
            false
        }
    }
}
