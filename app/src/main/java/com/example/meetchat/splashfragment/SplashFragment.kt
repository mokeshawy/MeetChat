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
import kotlinx.coroutines.*

class SplashFragment : Fragment() {

    lateinit var binding : FragmentSplashBinding
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSplashBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //  delay open splash fragment to viewPager fragment.
        CoroutineScope(Dispatchers.Main).launch{
            delay(1500)
            findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
        }
    }
}