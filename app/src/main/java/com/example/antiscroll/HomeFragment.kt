package com.example.antiscroll

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.antiscroll.databinding.FragmentHomeBinding
import com.example.antiscroll.db.TimeTrackingDatabase
import com.example.antiscroll.repository.AvailableAppSettingRepository
import com.example.antiscroll.repository.TimeTrackingRepository
import com.example.antiscroll.uiUtils.ChartsUtils
import com.example.antiscroll.uiUtils.SystemUtils
import com.example.antiscroll.uiUtils.UIUpdater
import com.example.antiscroll.viewmodel.AvailableAppSettingViewModel
import com.example.antiscroll.viewmodel.AvailableAppSettingViewModelFactory
import com.example.antiscroll.viewmodel.TimeTrackingViewModel
import com.example.antiscroll.viewmodel.TimeTrackingViewModelFactory


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: TimeTrackingViewModel
    private lateinit var availableAppViewModel: AvailableAppSettingViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        binding = FragmentHomeBinding.inflate(inflater, container, false)


        variableInit()
        subscribeClickListener()
        subscribeUI()


        // Inflate the layout for this fragment
        return binding.root
    }

    private fun variableInit() {

        // ************ Initialize the ViewModel ************

        // 1. Create an instance of the TimeTrackingRepository
        val repository = TimeTrackingRepository(TimeTrackingDatabase.getDataBase(requireContext()).timeTrackingDao())
        val factory = TimeTrackingViewModelFactory(repository)
        viewModel = ViewModelProvider(this@HomeFragment, factory)[TimeTrackingViewModel::class.java]



        // 2. Create an instance of the AvailableAppSettingViewModel
        val availableAppSettingRepository = AvailableAppSettingRepository(TimeTrackingDatabase.getDataBase(requireContext()).availableAppSettingDao())
        val availableAppSettingFactory = AvailableAppSettingViewModelFactory(availableAppSettingRepository)
        availableAppViewModel = ViewModelProvider(this@HomeFragment, availableAppSettingFactory)[AvailableAppSettingViewModel::class.java]
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


        // Call the getTotalDuration function and observe the result
        viewModel.getTotalDuration { totalDurationLiveData ->
            // Observing the LiveData to extract the Long value
            totalDurationLiveData.observe(viewLifecycleOwner) { totalDuration ->
                totalDuration?.let {

                    Log.d("HomeFragment", "Total Duration: $it")
                    // Now you have the Long value, use it here
                    ChartsUtils.pieChart(binding, SystemUtils.formatMillisToHHMM(it))
                }
            }
        }


        // Set the Pie Chart

        // Update the available app list
        UIUpdater.getAvilableAppList(
            context = requireContext(),
            binding = binding,
            availableAppViewModel = availableAppViewModel,
            viewLifecycleOwner = viewLifecycleOwner
        )
    }


    override fun onResume() {
        super.onResume()

        // Update the service status when returning from settings
        UIUpdater.updateServiceStatus(requireContext(), binding)
    }


}