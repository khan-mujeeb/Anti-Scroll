package com.example.antiscroll.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.antiscroll.R
import com.example.antiscroll.data.TimeTracking
import com.example.antiscroll.databinding.ActivityMainBinding
import com.example.antiscroll.db.TimeTrackingDatabase
import com.example.antiscroll.repository.TimeTrackingRepository
import com.example.antiscroll.uiUtils.UIUpdater
import com.example.antiscroll.viewmodel.TimeTrackingViewModel
import com.example.antiscroll.viewmodel.TimeTrackingViewModelFactory


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    public lateinit var viewModelPublic: TimeTrackingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        variableInit()
        subscribeUI()

    }


    // Function to load the selected fragment into the FrameLayout
    private fun loadFragment() {

        NavigationUI.setupWithNavController(binding.bottomBar, navController)

        // Load the selected fragment into the FrameLayout
        binding.bottomBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
                    binding.toolbarTitle.text = getString(R.string.app_name)
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.statsFragment -> {
                    binding.toolbarTitle.text = getString(R.string.weekly_usage)
                    navController.navigate(R.id.statsFragment)
                    true
                }
                else -> false
            }
        }
    }



    private fun variableInit() {
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Assuming repository is initialized somewhere in your code
        val repository = TimeTrackingRepository(TimeTrackingDatabase.Companion.getDataBase(this).timeTrackingDao())
        val factory = TimeTrackingViewModelFactory(repository)

        viewModelPublic = ViewModelProvider(this, factory)[TimeTrackingViewModel::class.java]

    }

    private fun subscribeUI() {




        // Load the Fragment
        loadFragment()




    }




}
