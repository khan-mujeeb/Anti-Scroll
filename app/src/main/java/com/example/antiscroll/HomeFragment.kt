package com.example.antiscroll

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.antiscroll.databinding.FragmentHomeBinding
import com.example.antiscroll.uiUtils.ChartsUtils
import com.example.antiscroll.uiUtils.UIUpdater


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


        // Check if the Accessibility Service is enabled
        UIUpdater.updateServiceStatus(requireContext(), binding)

        // Set the Pie Chart
        ChartsUtils.pieChart(binding)

        // Update the available app list
        UIUpdater.getAvilableAppList(requireContext(), binding)
    }


    override fun onResume() {
        super.onResume()

        // Update the service status when returning from settings
        UIUpdater.updateServiceStatus(requireContext(), binding)
    }


}