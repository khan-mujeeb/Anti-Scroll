package com.example.antiscroll.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.antiscroll.R
import com.example.antiscroll.databinding.ActivityMainBinding
import com.example.antiscroll.uiUtils.UIUpdater


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController

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
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.statsFragment -> {
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

    }

    private fun subscribeUI() {

        // Load the Fragment
        loadFragment()




    }




}
