package com.example.meetchat.viewpagerfragment

import android.content.Context
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.viewpager2.widget.ViewPager2
import com.example.meetchat.adapter.ViewPagerAdapter
import com.example.meetchat.chatsfragment.ChatsFragment
import com.example.meetchat.searchfragment.SearchFragment
import com.example.meetchat.settingsfragment.SettingsFragment
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class ViewPagerViewModel  : ViewModel(){

    fun viewPagerWork(viewPager2: ViewPager2 , tabLayout : TabLayout , fragmentManager: FragmentManager , lifecycle: Lifecycle ){

        // operation work for viewPager2 with fragment
        val fragmentsList = arrayListOf<Fragment>( ChatsFragment() , SearchFragment() , SettingsFragment())
        val adapter = ViewPagerAdapter( fragmentsList , fragmentManager , lifecycle)
        viewPager2.adapter = adapter

        // operation work for tabLayOut with viewPager2
        TabLayoutMediator( tabLayout , viewPager2){tab,position ->
            when(position){
                0 ->{
                    tab.text = "Chats"
                }
                1 ->{
                    tab.text = "Search"
                }
                2 ->{
                    tab.text = "Setting"
                }
            }
        }.attach()

    }
}
