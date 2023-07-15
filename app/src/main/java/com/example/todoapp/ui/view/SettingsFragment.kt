package com.example.todoapp.ui.view

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RadioGroup
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment
import com.example.todoapp.R
import com.example.todoapp.databinding.FragmentSettingsBinding
import com.example.todoapp.utils.navigator


class SettingsFragment : Fragment() {
    private var _binding: FragmentSettingsBinding? = null
    private val binding get() = _binding!!
    private lateinit var themeRadioGroup: RadioGroup
    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        themeRadioGroup = binding.themeRadioGroup
        themeRadioGroup.setOnCheckedChangeListener { _, checkedId ->
            handleThemeSelection(checkedId)
        }

        sharedPreferences = requireActivity().getSharedPreferences("ThemePrefs", Context.MODE_PRIVATE)
        when (sharedPreferences.getString("selectedTheme", "system")){
            "light" -> themeRadioGroup.check(R.id.lightMode)
            "system" -> themeRadioGroup.check(R.id.systemMode)
            "dark" -> themeRadioGroup.check(R.id.darkMode)
        }

        binding.save.setOnClickListener { onCancelPressed() }

        return binding.root
    }

    private fun handleThemeSelection(checkedId: Int) {
        when (checkedId) {
            R.id.lightMode -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                saveThemeSelection("light")
            }
            R.id.systemMode -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_FOLLOW_SYSTEM)
                saveThemeSelection("system")
            }
            R.id.darkMode -> {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                saveThemeSelection("dark")
            }
        }
    }

    private fun onCancelPressed() {
        navigator().showList()
    }

    private fun saveThemeSelection(theme: String) {
        sharedPreferences.edit().putString("selectedTheme", theme).apply()
    }
}