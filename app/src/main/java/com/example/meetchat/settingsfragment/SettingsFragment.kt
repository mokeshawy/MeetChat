package com.example.meetchat.settingsfragment

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.fragment.app.viewModels
import com.example.meetchat.R
import com.example.meetchat.activitis.MainActivity
import com.example.meetchat.databinding.FragmentSettingsBinding
import com.example.meetchat.model.UsersModel
import com.example.meetchat.util.Constants
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import java.util.jar.Manifest

class SettingsFragment : Fragment() {

    lateinit var binding            : FragmentSettingsBinding
    private val settingsViewModel   : SettingsViewModel by viewModels()
    lateinit var coverUri           : Uri
    lateinit var profileUri         : Uri
             var socialChecker      : String = ""

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

        // Default uri for image cover and profile.
        coverUri    = Constants.DEFAULT_COVER_IMAGE.toUri()
        profileUri  = Constants.DEFAULT_IMAGE_PROFILE.toUri()


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

            // Show progress dialog when click button save change after select image cover or profile.
            Constants.showProgressDialog(resources.getString(R.string.please_wait) ,requireActivity())
            // Call function select cover image.
            settingsViewModel.uploadCoverImageToDatabase( coverUri, binding.btnSaveChange)

            // Call function select profile image.
            settingsViewModel.uploadProfileImageToDatabase( profileUri, binding.btnSaveChange)

        }

        //Call function for get details data from database and show in setting view.
        settingsViewModel.getUserDetails(requireActivity(),
            binding.tvUsernameSettings ,
            binding.ivProfileSettings ,
            binding.ivCoverSettings)

        // Set facebook Url
        binding.setFacebook.setOnClickListener {
            socialChecker = Constants.USER_FACEBOOK
            settingsViewModel.setSocialLink( requireActivity() , socialChecker)
        }
        // Set instagram Url
        binding.setInstagram.setOnClickListener {
            socialChecker = Constants.USER_INSTAGRAM
            settingsViewModel.setSocialLink( requireActivity() , socialChecker)
        }
        // Set website Url
        binding.setWebsite.setOnClickListener {
            socialChecker = Constants.USER_WEBSITE
            settingsViewModel.setSocialLink( requireActivity() , socialChecker)
        }

    }

    //function select cover image
    private fun pickCoverImage(){
        // Permission
        if(ContextCompat.checkSelfPermission(requireActivity() , android.Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity() , arrayOf(android.Manifest.permission.READ_EXTERNAL_STORAGE) , 1)
        }else{
            var intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult( intent,Constants.PICK_COVER_IMAGE_REQUEST )

            CoroutineScope(Dispatchers.Main).launch {
                Constants.updateStatus("Online")
            }
        }
    }

    //function select profile image
    private fun pickProfileImage(){
        // Permission
        if(ContextCompat.checkSelfPermission(requireActivity() , android.Manifest.permission.READ_EXTERNAL_STORAGE) !=PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(requireActivity() , arrayOf( android.Manifest.permission.READ_EXTERNAL_STORAGE) , 2)
        }else{
            var intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent , Constants.PICK_PROFILE_IMAGE_REQUEST)
        }

        CoroutineScope(Dispatchers.Main).launch {
            Constants.updateStatus("Online")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        // Request from select cover image
        if(requestCode == Constants.PICK_COVER_IMAGE_REQUEST && resultCode == AppCompatActivity.RESULT_OK ){
            coverUri = data?.data!!
            binding.ivCoverSettings.setImageURI(coverUri)
            binding.btnSaveChange.visibility = View.VISIBLE
        }
        // Request from select profile image
        if(requestCode ==  Constants.PICK_PROFILE_IMAGE_REQUEST && resultCode == AppCompatActivity.RESULT_OK ){
            profileUri = data?.data!!
            binding.ivProfileSettings.setImageURI(profileUri)
            binding.btnSaveChange.visibility = View.VISIBLE
        }
    }
}