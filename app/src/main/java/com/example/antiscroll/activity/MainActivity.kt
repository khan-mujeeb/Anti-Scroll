package com.example.antiscroll.activity

import android.content.Context
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.NavigationUI
import com.example.antiscroll.R
import com.example.antiscroll.data.AvailableAppSetting
import com.example.antiscroll.data.BlockScrollAppList.defaultSettings
import com.example.antiscroll.databinding.ActivityMainBinding
import com.example.antiscroll.db.TimeTrackingDatabase
import com.example.antiscroll.repository.AvailableAppSettingRepository
import com.example.antiscroll.viewmodel.AvailableAppSettingViewModel
import com.example.antiscroll.viewmodel.AvailableAppSettingViewModelFactory
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var navController: NavController
    private lateinit var availableAppViewModel: AvailableAppSettingViewModel
    private val Context.dataStore by preferencesDataStore(name = "app_preferences")


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

        // Get the NavController
        navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // view model

        // available app setting view model
        val availableAppSettingRepository = AvailableAppSettingRepository(TimeTrackingDatabase.getDataBase(this@MainActivity).availableAppSettingDao())
        val availableAppSettingFactory = AvailableAppSettingViewModelFactory(availableAppSettingRepository)
        availableAppViewModel = ViewModelProvider(this, availableAppSettingFactory)[AvailableAppSettingViewModel::class.java]

    }

    private fun subscribeUI() {


        // handle the first run
        checkAndSetUpForFirstTimeUser(this@MainActivity)


        // Load the Fragment
        loadFragment()




    }


    suspend fun saveFirstRun(context: Context, isFirstRun: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_FIRST_RUN] = isFirstRun
        }
    }


    private fun isFirstRun(context: Context): Flow<Boolean> {
        return context.dataStore.data
            .map { preferences ->
                preferences[PreferencesKeys.IS_FIRST_RUN] ?: true // Default to true for first run
            }
    }


    private fun checkAndSetUpForFirstTimeUser(context: Context) {
        runBlocking {
            val isFirstTime = isFirstRun(context).first() // Get the value from DataStore

            if (isFirstTime) {
                // Initialize default settings in the Room database
                insertDefaultAppSettings()

                // Save that it's no longer the first run
                saveFirstRun(context, false)
            }
        }
    }

    private suspend fun insertDefaultAppSettings() {


        // Insert these settings into the Room database
        availableAppViewModel.insertDefaultSettings(defaultSettings)

    }


    object PreferencesKeys {
        val IS_FIRST_RUN = booleanPreferencesKey("is_first_run")
        // Add more keys here as needed
    }



}
