package com.example.antiscroll

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.provider.Settings
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.antiscroll.databinding.FragmentHomeBinding
import com.example.antiscroll.uiUtils.UIUpdater
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)



        subscribeClickListener()
        subscribeUI()





        // Inflate the layout for this fragment
        return binding.root
    }

    private fun subscribeClickListener() {


        // Set the click listener for the Enable Service button
        binding.enableServiceButton.setOnClickListener {

            // Redirect the user to the Accessibility Settings to enable the service
            val intent = Intent(Settings.ACTION_ACCESSIBILITY_SETTINGS)
            startActivity(intent)
        }



    }

    private fun subscribeUI() {

        // Load the Fragment



        // Check if the Accessibility Service is enabled
        UIUpdater.updateServiceStatus(requireContext(), binding)
        pieChart()
        UIUpdater.getAvilableAppList(requireContext(), binding)
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
        dataSet.sliceSpace = 3f
        dataSet.selectionShift = 5f

        // Apply colors that match the dark theme of your app
        dataSet.colors = listOf(
            Color.parseColor("#FFBB86FC"), // Light purple
            Color.parseColor("#FF6200EE"), // Purple
            Color.parseColor("#FF03DAC5"), // Teal
            Color.parseColor("#FF018786")  // Dark teal
        )

        // Create PieData
        val data = PieData(dataSet)
        data.setValueTextSize(14f)
        data.setValueTextColor(Color.WHITE) // Adjusted to suit your dark background

        // General Pie Chart settings
        binding.pieChart.setUsePercentValues(true)
        binding.pieChart.description.isEnabled = false // Disable description text
        binding.pieChart.setExtraOffsets(5f, 10f, 5f, 5f)
        binding.pieChart.dragDecelerationFrictionCoef = 0.95f

        // Customizing the chart appearance
        binding.pieChart.setDrawHoleEnabled(true)
        binding.pieChart.setHoleColor(Color.TRANSPARENT) // Hole color matching your background
        binding.pieChart.setTransparentCircleColor(Color.WHITE) // Light transparent circle
        binding.pieChart.setTransparentCircleAlpha(110)
        binding.pieChart.holeRadius = 58f
        binding.pieChart.transparentCircleRadius = 61f

        // Text in the center of the PieChart
        binding.pieChart.setDrawCenterText(true)
        binding.pieChart.centerText = "Your Chart Title"
        binding.pieChart.setCenterTextSize(18f)
        binding.pieChart.setCenterTextColor(Color.WHITE)

        // Disable legend and entry labels to create a clean, modern look
        binding.pieChart.legend.isEnabled = false
        binding.pieChart.setEntryLabelColor(Color.WHITE)
        binding.pieChart.setEntryLabelTextSize(12f)

        // Set the data for the PieChart
        binding.pieChart.data = data

        // Refresh the PieChart
        binding.pieChart.invalidate() // refresh the chart
    }


    override fun onResume() {
        super.onResume()

        // Update the service status when returning from settings
        UIUpdater.updateServiceStatus(requireContext(), binding)
    }



}