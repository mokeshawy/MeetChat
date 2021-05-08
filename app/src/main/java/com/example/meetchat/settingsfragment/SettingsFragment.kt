package com.example.meetchat.settingsfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.meetchat.R
import com.example.meetchat.databinding.FragmentSettingsBinding

class SettingsFragment : Fragment() {

    lateinit var binding : FragmentSettingsBinding
    private val settingsViewModel : SettingsViewModel by viewModels()
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSettingsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Connect with settings view model
        binding.lifecycleOwner      = this
        binding.settingsVarModel    = settingsViewModel
    }
}