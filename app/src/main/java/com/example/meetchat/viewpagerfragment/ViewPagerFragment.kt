package com.example.meetchat.viewpagerfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.meetchat.adapter.ViewPagerAdapter
import com.example.meetchat.chatsfragment.ChatsFragment
import com.example.meetchat.databinding.FragmentViewPagerBinding
import com.example.meetchat.searchfragment.SearchFragment
import com.example.meetchat.settingsfragment.SettingsFragment
import com.google.android.material.tabs.TabLayoutMediator

class ViewPagerFragment : Fragment() {

    lateinit var binding : FragmentViewPagerBinding
    private val viewPagerViewModel : ViewPagerViewModel by viewModels()
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        binding = FragmentViewPagerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // connect with view model for viewPager
        binding.lifecycleOwner      = this
        binding.viewPagerVarModel   = viewPagerViewModel

        // operation viewPager with tabLayout.
        viewPagerViewModel.viewPagerWork(binding.viewPager,
            binding.tabLayout,
            requireActivity().supportFragmentManager,
            lifecycle)


    }
}