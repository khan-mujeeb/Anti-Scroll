package com.example.antiscroll

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView
import com.example.antiscroll.adapter.NoScrollAppAdapter
import com.example.antiscroll.data.BlockScrollAppList
import com.example.antiscroll.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        subscribeUI()
        variableInit()
        subscribeClickListener()







    }

    private fun subscribeClickListener() {

        // Set the click listener for the Enable Service button
        binding.enableServiceButton.setOnClickListener {
            // Redirect the user to the Accessibility Settings to enable the service
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            startActivity(intent)
        }
    }

    private fun variableInit() {

    }
    private fun subscribeUI() {

        // Check if the Accessibility Service is enabled
        accessiblityServiceStatus()

        getAvilableAppList()
    }

    private fun accessiblityServiceStatus() {
        if (isAccessibilityServiceEnabled()) {
            binding.enableServiceButton.text = "Close Service"
        } else {
            binding.enableServiceButton.text = "Start Service"
        }
    }

    private fun getAvilableAppList() {

        binding.noScrollAppRc.adapter = NoScrollAppAdapter(this@MainActivity, BlockScrollAppList.appList)

    }

    // Function to check if the Accessibility Service is enabled
    private fun isAccessibilityServiceEnabled(): Boolean {
        val service = "$packageName/${MyAccessibilityService::class.java.canonicalName}"
        return try {
            val enabled = Settings.Secure.getInt(contentResolver, Settings.Secure.ACCESSIBILITY_ENABLED)
            if (enabled == 1) {
                val settingValue = Settings.Secure.getString(contentResolver, Settings.Secure.ENABLED_ACCESSIBILITY_SERVICES)
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

    override fun onResume() {
        super.onResume()
        // Update the service status when returning from settings
        accessiblityServiceStatus()
    }
}
