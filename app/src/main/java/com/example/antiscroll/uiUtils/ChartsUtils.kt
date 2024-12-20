package com.example.antiscroll.uiUtils

import android.content.Context
import android.content.pm.PackageManager
import android.graphics.Color
import com.example.antiscroll.databinding.FragmentHomeBinding
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.ValueFormatter

object ChartsUtils {


    /// *********** function to update the pie chart ************
    fun pieChart(context: Context, binding: FragmentHomeBinding, durationByAppName: Map<String, Long>, centerText: String = "Total Usage") {


        val packageManager: PackageManager = context.packageManager


        // Prepare the data for the PieChart
        val pieEntries = durationByAppName.map { (appName, duration) ->

            val appInfo = packageManager.getApplicationInfo(appName, 0)

            PieEntry(duration.toFloat(), packageManager.getApplicationLabel(appInfo).toString() )
        }

        // Set up the dataset
        val dataSet = PieDataSet(pieEntries, "")
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f

        // Apply colors that match the theme of your app
        dataSet.colors = listOf(
            Color.parseColor("#FFBB86FC"), // Light purple
            Color.parseColor("#FF6200EE"), // Purple
            Color.parseColor("#FF03DAC5"), // Teal
            Color.parseColor("#FF018786")  // Dark teal
        )

        // Configure the value lines to draw labels outside the chart
        dataSet.valueLinePart1OffsetPercentage =
            80f // Distance between the line start and the chart
        dataSet.valueLinePart1Length = 0.15f // Length of the first segment of the line
        dataSet.valueLinePart2Length = 0.35f // Length of the second segment of the line
        dataSet.valueLineWidth = 1.35f // Width of the lines
        dataSet.valueLineColor = Color.BLACK // Color of the connecting lines
        dataSet.yValuePosition = PieDataSet.ValuePosition.OUTSIDE_SLICE // Position labels outside the slice
        dataSet.xValuePosition =
            PieDataSet.ValuePosition.OUTSIDE_SLICE // Horizontal position outside the slice

        // Set up the data object
        val data = PieData(dataSet)
        data.setDrawValues(false) // Disable drawing values on the chart
        data.setValueTextSize(12f)
        data.setValueTextColor(Color.BLACK) // Set label text color


        // General Pie Chart settings
        binding.pieChart.setUsePercentValues(true)
        binding.pieChart.description.isEnabled = false
        binding.pieChart.setExtraOffsets(5f, 10f, 5f, 5f)
        binding.pieChart.dragDecelerationFrictionCoef = 0.95f

        // Customizing the chart appearance
        binding.pieChart.isDrawHoleEnabled = true
        binding.pieChart.setHoleColor(Color.TRANSPARENT) // Hole color matching your background
        binding.pieChart.setTransparentCircleColor(Color.WHITE) // Light transparent circle
        binding.pieChart.setTransparentCircleAlpha(110)
        binding.pieChart.holeRadius = 85f
        binding.pieChart.transparentCircleRadius = 65f
        binding.pieChart.setEntryLabelColor(Color.BLACK) // Label text color

        // Text in the center of the PieChart
        binding.pieChart.setDrawCenterText(true)
        binding.pieChart.centerText = centerText
        binding.pieChart.setCenterTextSize(18f)
        binding.pieChart.setCenterTextColor(Color.BLACK)

        // Customizing the legend appearance
        binding.pieChart.legend.isEnabled = false
        binding.pieChart.legend.textSize = 16f
        binding.pieChart.legend.formSize = 16f
        binding.pieChart.legend.horizontalAlignment = Legend.LegendHorizontalAlignment.CENTER
        binding.pieChart.legend.yOffset = 20f
        binding.pieChart.legend.xEntrySpace = 20f

        // Set the data for the PieChart
        binding.pieChart.data = data

        // Refresh the PieChart
        binding.pieChart.invalidate() // Refresh the chart
    }

}