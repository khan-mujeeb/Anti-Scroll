package com.example.antiscroll

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import android.widget.Button
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    private lateinit var statusTextView: TextView
    private lateinit var enableServiceButton: Button
    private lateinit var customizeBlockingButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        statusTextView = findViewById(R.id.statusTextView)
        enableServiceButton = findViewById(R.id.enableServiceButton)
        customizeBlockingButton = findViewById(R.id.customizeBlockingButton)

        // Check if the Accessibility Service is enabled
        if (isAccessibilityServiceEnabled()) {
            statusTextView.text = "Service Status: Enabled"
        } else {
            statusTextView.text = "Service Status: Disabled"
        }

        enableServiceButton.setOnClickListener {
            // Redirect the user to the Accessibility Settings to enable the service
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            startActivity(intent)
        }

        customizeBlockingButton.setOnClickListener {
            // Handle customization settings (like opening a new settings activity)
            // Example: Open a new activity for customizing blocking rules
            val intent = Intent(this, CustomizeBlockingActivity::class.java)
            startActivity(intent)
        }
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
        if (isAccessibilityServiceEnabled()) {
            statusTextView.text = "Service Status: Enabled"
        } else {
            statusTextView.text = "Service Status: Disabled"
        }
    }
}
