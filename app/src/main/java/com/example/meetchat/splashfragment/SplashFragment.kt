package com.example.meetchat.splashfragment

import android.os.Bundle
import android.os.Handler
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.example.meetchat.R
import com.example.meetchat.databinding.FragmentSplashBinding

class SplashFragment : Fragment() {

    lateinit var binding : FragmentSplashBinding
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // handler splash fragment open to viewPager fragment
        @Suppress("DEPRECATION")
        Handler().postDelayed(
            {
                findNavController().navigate(R.id.action_splashFragment_to_loginFragment)

            }, 2000
        )
    }
}