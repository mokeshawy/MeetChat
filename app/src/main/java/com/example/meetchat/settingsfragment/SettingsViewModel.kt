package com.example.meetchat.settingsfragment

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.example.meetchat.R
import com.example.meetchat.util.Constants
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class SettingsViewModel : ViewModel(){

    var firebaseDatabase    = FirebaseDatabase.getInstance()
    var userReference       = firebaseDatabase.getReference(Constants.USER_REFERENCE)
    var firebaseStorage     = FirebaseStorage.getInstance().reference

    // function operation work for upload cover image
    fun uploadCoverImageToDatabase( context: Context , imageUri : Uri){
        var storage : StorageReference = firebaseStorage.child("Photo/"+System.currentTimeMillis())
        //Upload image to database.
        storage.putFile(imageUri).addOnCompleteListener { uploadImage ->
            if( uploadImage.isSuccessful){
                // Download image from database.
                storage.downloadUrl.addOnSuccessListener { imageUrl ->
                    var map = HashMap<String , Any>()
                    map[Constants.USER_COVER] = imageUrl.toString()
                    userReference.child(Constants.getCurrentUser()).updateChildren(map)
                    Constants.hideProgressDialog()
                }
            }else{
                Toast.makeText(context , context.getString(R.string.err_no_select_image),Toast.LENGTH_SHORT).show()
                Constants.hideProgressDialog()
            }
        }

    }

    // function operation work for upload profile image
    fun uploadProfileImageToDatabase( context: Context , imageUri : Uri){
        var storage : StorageReference = firebaseStorage.child("Photo/"+System.currentTimeMillis())
        //Upload image to data base.
        storage.putFile(imageUri).addOnCompleteListener { uploadImage ->
            if( uploadImage.isSuccessful){
                // Download image from database.
                storage.downloadUrl.addOnSuccessListener { imageUrl ->
                    var map = HashMap<String , Any>()
                    map[Constants.USER_PROFILE] = imageUrl.toString()
                    userReference.child(Constants.getCurrentUser()).updateChildren(map)
                    Constants.hideProgressDialog()
                }
            }else{
                Toast.makeText(context , context.getString(R.string.err_no_select_image),Toast.LENGTH_SHORT).show()
                Constants.hideProgressDialog()
            }
        }
    }

}