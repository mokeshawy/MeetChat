package com.example.meetchat.settingsfragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import com.example.meetchat.R
import com.example.meetchat.databinding.FragmentSettingsBinding
import com.example.meetchat.model.UsersModel
import com.example.meetchat.util.Constants

class SettingsFragment : Fragment() {

    lateinit var binding            : FragmentSettingsBinding
    private val settingsViewModel   : SettingsViewModel by viewModels()
    lateinit var imageUri           : Uri

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

        // Select cover image
        binding.ivCoverSettings.setOnClickListener {
            pickCoverImage()
        }

        // Select profile image
        binding.ivProfileSettings.setOnClickListener {
            pickProfileImage()
        }

        // btn for save change
        binding.btnSaveChange.setOnClickListener {
            settingsViewModel.uploadImageToDatabase(imageUri)
        }
        
    }

    //function select cover image
    private fun pickCoverImage(){
        var intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult( intent,Constants.PICK_COVER_IMAGE_REQUEST )


    }
    //function select profile image
    private fun pickProfileImage(){
        var intent = Intent(Intent.ACTION_PICK)
        intent.type = "image/*"
        startActivityForResult(intent , Constants.PICK_PROFILE_IMAGE_REQUEST)

    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Request from select cover image
        if(requestCode == Constants.PICK_COVER_IMAGE_REQUEST && resultCode == AppCompatActivity.RESULT_OK && data?.data!! !=null){
            imageUri = data?.data!!
            binding.ivCoverSettings.setImageURI(imageUri)
            binding.btnSaveChange.visibility = View.VISIBLE
        }
        // Request from select profile image
        if(requestCode ==  Constants.PICK_PROFILE_IMAGE_REQUEST && resultCode == AppCompatActivity.RESULT_OK && data?.data!! !=null){
            imageUri = data?.data!!
            binding.ivProfileSettings.setImageURI(imageUri)
            binding.btnSaveChange.visibility = View.VISIBLE
        }
    }

}