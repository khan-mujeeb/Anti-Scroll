package com.example.antiscroll

import android.content.Intent
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import com.example.antiscroll.data.AvailableAppSetting
import com.example.antiscroll.data.BlockScrollAppList
import com.example.antiscroll.data.TimeQuotes.getQuote
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
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch


class HomeFragment : Fragment() {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var viewModel: TimeTrackingViewModel
    private lateinit var availableAppViewModel: AvailableAppSettingViewModel
//    private lateinit var enabledAppList: List<AvailableAppSetting>

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
        val availableAppSettingRepository = AvailableAppSettingRepository(
            TimeTrackingDatabase
            .getDataBase(requireContext())
            .availableAppSettingDao()
        )
        val availableAppSettingFactory = AvailableAppSettingViewModelFactory(availableAppSettingRepository)

        availableAppViewModel = ViewModelProvider(
            this@HomeFragment,
            availableAppSettingFactory)[AvailableAppSettingViewModel::class.java]



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

        // set new quote
        setQuote()


        // Call the getTotalDuration function and observe the result
//        viewModel.getTotalDuration { totalDurationLiveData ->
//            // Observing the LiveData to extract the Long value
//            totalDurationLiveData.observe(viewLifecycleOwner) { totalDuration ->
//                totalDuration?.let {
//
//                    Log.d("HomeFragment", "Total Duration: $it")
//                    // Now you have the Long value, use it here
//                    ChartsUtils.pieChart(binding, SystemUtils.formatMillisToHHMM(it))
//                }
//            }
//        }


        // Collect the tracking data and prepare it for the chart
        viewModel.getAllTrackingData { allTrackingDataFlow ->
            lifecycleScope.launch {
                allTrackingDataFlow.collect { trackingDataList ->
                    val durationByAppName = trackingDataList.groupBy { it.appName }
                        .mapValues { (_, entries) -> entries.sumOf { it.duration } }

                    // Format total usage time (optional)
                    val totalUsageText = SystemUtils.formatMillisToHHMM(durationByAppName.values.sum())

                    // Pass the data to the chart
                    ChartsUtils.pieChart(requireContext(), binding, durationByAppName, totalUsageText)
                }
            }
        }





        // Update the available app list
        getAviableAppList()

    }

    private fun getAviableAppList() {
        lifecycleScope.launch {
            lifecycle.repeatOnLifecycle(Lifecycle.State.STARTED) {
                availableAppViewModel.getAllSettings()
                    .distinctUntilChanged()  // This will only emit changes when the data actually changes
                    .collect { availableAppList ->

                        UIUpdater.getAvilableAppList(
                            context = requireContext(),
                            binding = binding,
                            availableAppViewModel = availableAppViewModel,
                            viewLifecycleOwner = viewLifecycleOwner,
                            availableAppList = availableAppList
                        )

//                        BlockScrollAppList.appSettingList = availableAppList.filter { it.isAntiScrollEnabled }
                    }
            }
        }
    }

    private fun setQuote() {
        val quote = getQuote()
        binding.quoteTextView.text = quote.text
        binding.authorNameTextView.text = "~ ${quote.author}"
    }


    override fun onResume() {
        super.onResume()

        // Update the service status when returning from settings
        UIUpdater.updateServiceStatus(requireContext(), binding)
    }


}