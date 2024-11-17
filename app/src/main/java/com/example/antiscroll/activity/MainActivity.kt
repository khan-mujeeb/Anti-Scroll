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
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.antiscroll.R
import com.example.antiscroll.data.BlockScrollAppList.defaultSettings
import com.example.antiscroll.databinding.ActivityMainBinding
import com.example.antiscroll.db.TimeTrackingDatabase
import com.example.antiscroll.repository.AvailableAppSettingRepository
import com.example.antiscroll.viewmodel.AvailableAppSettingViewModel
import com.example.antiscroll.viewmodel.AvailableAppSettingViewModelFactory
import com.example.antiscroll.worker.ClearDatabaseWorker
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.runBlocking
import java.util.Calendar
import java.util.concurrent.TimeUnit

// DataStore instance as a context extension property
private val Context.dataStore by preferencesDataStore(name = "app_preferences")

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var navController: NavController
    private lateinit var availableAppViewModel: AvailableAppSettingViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initVariables()
        setupNavigation()
        checkAndSetUpForFirstTimeUser(this)
        scheduleDailyDatabaseClear()
    }


    private fun scheduleDailyDatabaseClear() {
        val workRequest = PeriodicWorkRequestBuilder<ClearDatabaseWorker>(1, TimeUnit.DAYS)
            .setInitialDelay(calculateInitialDelay(), TimeUnit.MILLISECONDS) // Delay till 12 AM
            .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "ClearDatabaseWork",
            ExistingPeriodicWorkPolicy.REPLACE, // Replace existing work if already scheduled
            workRequest
        )
    }

    private fun calculateInitialDelay(): Long {
        val currentTime = System.currentTimeMillis()
        val calendar = Calendar.getInstance().apply {
            timeInMillis = currentTime
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
            if (timeInMillis <= currentTime) {
                add(Calendar.DAY_OF_YEAR, 1) // Schedule for the next day
            }
        }
        return calendar.timeInMillis - currentTime
    }

    // Function to initialize required variables and ViewModel
    private fun initVariables() {
        // Get the NavController
        val navHostFragment = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController = navHostFragment.navController

        // Set up ViewModel
        val availableAppSettingRepository = AvailableAppSettingRepository(
            TimeTrackingDatabase.getDataBase(this).availableAppSettingDao()
        )
        val availableAppSettingFactory = AvailableAppSettingViewModelFactory(availableAppSettingRepository)
        availableAppViewModel = ViewModelProvider(this, availableAppSettingFactory)[AvailableAppSettingViewModel::class.java]
    }

    // Set up bottom navigation and fragment switching
    private fun setupNavigation() {
        NavigationUI.setupWithNavController(binding.bottomBar, navController)
        binding.bottomBar.setOnNavigationItemSelectedListener {
            when (it.itemId) {
                R.id.homeFragment -> {
//                    binding.toolbarTitle.text = getString(R.string.app_name)
                    navController.navigate(R.id.homeFragment)
                    true
                }
                R.id.statsFragment -> {
//                    binding.toolbarTitle.text = getString(R.string.weekly_usage)
                    navController.navigate(R.id.statsFragment)
                    true
                }
                else -> false
            }
        }
    }

    // Function to handle first-time user setup
    private fun checkAndSetUpForFirstTimeUser(context: Context) {
        runBlocking {
            val isFirstTime = isFirstRun(context).first()

            if (isFirstTime) {
                insertDefaultAppSettings()
                saveFirstRun(context, false)
            }
        }
    }

    // Save first run status in DataStore
    private suspend fun saveFirstRun(context: Context, isFirstRun: Boolean) {
        context.dataStore.edit { preferences ->
            preferences[PreferencesKeys.IS_FIRST_RUN] = isFirstRun
        }
    }

    // Check if the app is running for the first time
    private fun isFirstRun(context: Context): Flow<Boolean> {
        return context.dataStore.data
            .map { preferences ->
                preferences[PreferencesKeys.IS_FIRST_RUN] ?: true // Default to true for first run
            }
    }

    // Insert default app settings into the database
    private suspend fun insertDefaultAppSettings() {
        availableAppViewModel.insertDefaultSettings(defaultSettings)
    }

    // Preference keys used in DataStore
    object PreferencesKeys {
        val IS_FIRST_RUN = booleanPreferencesKey("is_first_run")
    }
}
