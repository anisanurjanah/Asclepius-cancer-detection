package com.anisanurjanah.asclepius.view.fragment

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.appcompat.widget.Toolbar
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.anisanurjanah.asclepius.R
import com.anisanurjanah.asclepius.databinding.FragmentProfileBinding
import com.anisanurjanah.asclepius.helper.Setting
import com.anisanurjanah.asclepius.helper.SettingPreferences
import com.anisanurjanah.asclepius.helper.SettingViewModelFactory
import com.anisanurjanah.asclepius.model.SettingViewModel
import com.anisanurjanah.asclepius.view.adapter.SettingAdapter

class ProfileFragment : Fragment() {
    private var _binding: FragmentProfileBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    private val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "settings")

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvSettingsProfile.setHasFixedSize(true)
        showListSettingsProfile()

        binding.rvSettingsProfile.setHasFixedSize(true)
        showListSettingsApp()

        themeSettings()
    }

    private fun getListSettingsProfile(): ArrayList<Setting> {
        val settingProfile = resources.getStringArray(R.array.data_setting_profile)
        val listSetting = ArrayList<Setting>()
        for (i in settingProfile.indices) {
            val setting = Setting(settingProfile[i])
            listSetting.add(setting)
        }
        return listSetting
    }

    private fun showListSettingsProfile() {
        binding.rvSettingsProfile.layoutManager = LinearLayoutManager(requireActivity())
        val listSettingProfile = getListSettingsProfile()

        val adapter = SettingAdapter(listSettingProfile)
        binding.rvSettingsProfile.adapter = adapter
    }

    private fun getListSettingsApp(): ArrayList<Setting> {
        val settingApp = resources.getStringArray(R.array.data_setting_app)
        val listSetting = ArrayList<Setting>()
        for (i in settingApp.indices) {
            val setting = Setting(settingApp[i])
            listSetting.add(setting)
        }
        return listSetting
    }

    private fun showListSettingsApp() {
        binding.rvSettingsApp.layoutManager = LinearLayoutManager(requireActivity())
        val listSettingApp = getListSettingsApp()

        val adapter = SettingAdapter(listSettingApp)
        binding.rvSettingsApp.adapter = adapter
    }

    private fun themeSettings() {
        val switchTheme = binding.switchTheme

        val pref = SettingPreferences.getInstance(requireContext().dataStore)
        val settingViewModel = ViewModelProvider(requireActivity(), SettingViewModelFactory(pref))[SettingViewModel::class.java]

        settingViewModel.getThemeSettings().observe(requireActivity()) { isDarkModeActive: Boolean ->
            if (isDarkModeActive) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                switchTheme.isChecked = true
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                switchTheme.isChecked = false
            }
        }

        switchTheme.setOnCheckedChangeListener { _: CompoundButton?, isChecked: Boolean ->
            settingViewModel.saveThemeSetting(isChecked)
        }
    }

    private fun setToolbar() {
        val toolbar = requireActivity().findViewById<Toolbar>(R.id.topAppBar)
        (requireActivity() as AppCompatActivity).setSupportActionBar(toolbar)

        toolbar.title = getString(R.string.profile)
        toolbar.setTitleTextAppearance(requireContext(), R.style.ToolbarTitleText)
    }

    override fun onResume() {
        super.onResume()
        setToolbar()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}