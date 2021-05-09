package com.example.meetchat.viewpagerfragment

import android.app.PendingIntent.getActivity
import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.meetchat.R
import com.example.meetchat.adapter.ViewPagerAdapter
import com.example.meetchat.chatsfragment.ChatsFragment
import com.example.meetchat.databinding.FragmentViewPagerBinding
import com.example.meetchat.searchfragment.SearchFragment
import com.example.meetchat.settingsfragment.SettingsFragment
import com.google.android.material.internal.ContextUtils.getActivity
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth

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


        // Call function for operation set on menu item
        viewPagerViewModel.setOnMenuItem( requireActivity(),
            view,
            binding.toolBarViewPager)
    }
}