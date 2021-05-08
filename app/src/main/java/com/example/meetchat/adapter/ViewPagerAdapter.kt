package com.example.meetchat.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter

class ViewPagerAdapter( items : ArrayList<Fragment> ,
                        fragmentManager: FragmentManager ,
                        lifecycle : Lifecycle ) : FragmentStateAdapter( fragmentManager , lifecycle){

    private val fragmentItems = items

    override fun getItemCount(): Int {
        return fragmentItems.size
    }

    override fun createFragment(position: Int): Fragment {

        return fragmentItems[position]
    }

}