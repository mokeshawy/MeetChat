package com.example.meetchat.visituserprofilefragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.os.bundleOf
import androidx.navigation.fragment.findNavController
import com.example.meetchat.R
import com.example.meetchat.databinding.FragmentVisitUserProfileBinding
import com.example.meetchat.model.UsersModel
import com.example.meetchat.util.Constants
import com.squareup.picasso.Picasso

class VisitUserProfileFragment : Fragment() {

    lateinit var binding    : FragmentVisitUserProfileBinding
    lateinit var userVisit  : UsersModel

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        binding = FragmentVisitUserProfileBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // receive object from chat page and search page.
         userVisit = arguments?.getSerializable(Constants.SERIALIZABLE_USERS_PROFILE) as UsersModel

        // show image profile and cover image.
        binding.tvUserNameDisplay.text = userVisit.username
        Picasso.get().load(userVisit.cover).into(binding.ivCoverDisplay)
        Picasso.get().load(userVisit.profile).into(binding.ivProfileDisplay)

        // open facebook for user visit.
        binding.ivSetFacebook.setOnClickListener {
            val uri = Uri.parse(userVisit.facebook)
            val intent = Intent(Intent.ACTION_VIEW,uri)
            startActivity(intent)
        }

        // open instagram for user visit.
        binding.ivSetInstagram.setOnClickListener {
            val uri = Uri.parse(userVisit.instagram)
            val intent = Intent(Intent.ACTION_VIEW,uri)
            startActivity(intent)
        }

        // open webSite for user visit.
        binding.ivSetWebsite.setOnClickListener {
            val uri = Uri.parse(userVisit.website)
            val intent = Intent(Intent.ACTION_VIEW,uri)
            startActivity(intent)
        }

        // show full image profile.
        binding.ivProfileDisplay.setOnClickListener {
            val bundle1 = bundleOf(Constants.SERIALIZABLE_USERS_PROFILE_SHOW_FULL_IMAGE to userVisit.profile)
            findNavController().navigate(R.id.action_visitUserProfileFragment_to_viewFullImageFragment , bundle1)
        }

        // show full image cover.
        binding.ivCoverDisplay.setOnClickListener {
            val bundle2 = bundleOf(Constants.SERIALIZABLE_USERS_PROFILE_SHOW_FULL_COVER to userVisit.cover)
            findNavController().navigate(R.id.action_visitUserProfileFragment_to_viewFullImageFragment , bundle2)
        }
    }
}