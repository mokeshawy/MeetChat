package com.example.meetchat.settingsfragment

import android.content.Context
import android.net.Uri
import androidx.lifecycle.ViewModel
import com.example.meetchat.util.Constants
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference

class SettingsViewModel : ViewModel(){

    var coverChecker    : String? = ""
    var socialChecker   : String? = ""

    var firebaseDatabase = FirebaseDatabase.getInstance()
    var userReference = firebaseDatabase.getReference(Constants.USER_REFERENCE)
    var firebaseStorage = FirebaseStorage.getInstance().reference

    fun uploadImageToDatabase( imageUri : Uri){

        if( imageUri != null){
            var storage : StorageReference = firebaseStorage.child("Photo/"+System.currentTimeMillis())
            //Upload image to data base
            storage.putFile(imageUri).addOnCompleteListener { uploadImage ->
                if( uploadImage.isSuccessful){
                    // Download image from data base
                    storage.downloadUrl.addOnSuccessListener { imageUrl ->

                        // when select image from cover will save urlImage in child cover in data base
                        if( coverChecker == Constants.USER_COVER){
                            var map = HashMap<String , Any>()
                            map[Constants.USER_COVER] = imageUrl.toString()
                            userReference.child(Constants.getCurrentUser()).updateChildren(map)
                        }else{  // when select image from profile will save urlImage in child profile in data base
                            var map = HashMap<String , Any>()
                            map[Constants.USER_PROFILE] = imageUrl.toString()
                            userReference.child(Constants.getCurrentUser()).updateChildren(map)
                        }
                    }
                }
            }
        }
    }
}