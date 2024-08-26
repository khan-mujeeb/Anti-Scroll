package com.example.antiscroll

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import android.text.TextUtils
import androidx.appcompat.app.AppCompatActivity
import com.example.antiscroll.adapter.NoScrollAppAdapter
import com.example.antiscroll.data.BlockScrollAppList
import com.example.antiscroll.databinding.ActivityMainBinding
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate

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
        pieChart()
        getAvilableAppList()
    }

    private fun pieChart() {
        // Initialize the PieChart

        // Prepare the data for the PieChart
        val pieEntries = listOf(
            PieEntry(30f, "January"),
            PieEntry(20f, "February"),
            PieEntry(50f, "March")
        )

        // Set up the dataset
        val dataSet = PieDataSet(pieEntries, "Monthly Data")
        dataSet.colors = ColorTemplate.COLORFUL_COLORS.asList() // Set the colors of the pie slices

        // Create PieData
        val data = PieData(dataSet)
        data.setValueTextSize(12f)
        data.setValueTextColor(Color.BLACK)

        // Set data to PieChart and customize the chart appearance
        binding.pieChart.data = data
        binding.pieChart.setUsePercentValues(true)  // Show data as percentages
        binding.pieChart.centerText = "Sales"
        binding.pieChart.setCenterTextSize(16f)
        binding.pieChart.description.isEnabled = false // Remove the description label

        binding.pieChart.legend.isEnabled = false // Hide the legend

        binding.pieChart.animateY(1000)
        binding.pieChart.setHoleColor(getColor(R.color.eerie_black))
        // Refresh the chart
        binding.pieChart.invalidate() // Refr

    }

    private fun accessiblityServiceStatus() {
        if (isAccessibilityServiceEnabled()) {
            binding.enableServiceButton.text = "Close Service"
            binding.enableServiceButton.setBackgroundColor(getColor(R.color.carmine))
        } else {
            binding.enableServiceButton.text = "Start Service"
            binding.enableServiceButton.setBackgroundColor(getColor(R.color.dark_spring_green))
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
