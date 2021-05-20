package com.example.meetchat.viewfullimagefragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.meetchat.R
import com.example.meetchat.databinding.FragmentViewFullImageBinding
import com.example.meetchat.util.Constants
import com.squareup.picasso.Picasso

class ViewFullImageFragment : Fragment() {

    lateinit var binding : FragmentViewFullImageBinding

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        binding = FragmentViewFullImageBinding.inflate(inflater)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        var image = arguments?.getString(Constants.FULL_IMAGE_VIEW)
        Picasso.get().load(image).into(binding.ivImageViewer)

    }
}