package com.anisanurjanah.asclepius.view.activity

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.app.AppCompatDelegate
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.anisanurjanah.asclepius.R
import com.anisanurjanah.asclepius.databinding.ActivityMainBinding
import com.anisanurjanah.asclepius.helper.SettingPreferences
import com.anisanurjanah.asclepius.helper.SettingViewModelFactory
import com.anisanurjanah.asclepius.model.SettingViewModel
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {
    private lateinit var binding: ActivityMainBinding

    private lateinit var settingPreferences: SettingPreferences
    private lateinit var settingViewModel: SettingViewModel

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(SETTING)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        settingPreferences = SettingPreferences.getInstance(dataStore)
        settingViewModel = ViewModelProvider(this, SettingViewModelFactory(settingPreferences))[SettingViewModel::class.java]
        settingViewModel.getThemeSettings().observe(this) { isDarkModeActive: Boolean ->
            themeSettings(isDarkModeActive)
        }

        setSupportActionBar(binding.topAppBar)

        val navView: BottomNavigationView = binding.navView
        val navController = findNavController(R.id.nav_host_fragment_activity_main)
        val appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_home,
                R.id.navigation_screening,
                R.id.navigation_article,
                R.id.navigation_profile
            )
        )
        setupActionBarWithNavController(navController, appBarConfiguration)
        navView.setupWithNavController(navController)
    }

    private fun themeSettings(isDarkModeActive: Boolean) {
        if (isDarkModeActive) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
        }
    }

    companion object {
        const val SETTING = "settings"
    }
}