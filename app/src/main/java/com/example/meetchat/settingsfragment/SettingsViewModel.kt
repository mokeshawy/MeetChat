package com.example.meetchat.settingsfragment

import android.app.AlertDialog
import android.content.Context
import android.net.Uri
import android.view.View
import android.widget.*
import androidx.lifecycle.ViewModel
import com.example.meetchat.R
import com.example.meetchat.model.UsersModel
import com.example.meetchat.util.Constants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class SettingsViewModel : ViewModel(){

    var firebaseDatabase    = FirebaseDatabase.getInstance()
    var userReference       = firebaseDatabase.getReference(Constants.USER_REFERENCE)
    var firebaseStorage     = FirebaseStorage.getInstance().reference

    // function operation work for upload cover image
    fun uploadCoverImageToDatabase( imageUri : Uri,
                                    btn_save_change : Button){
        var storage : StorageReference = firebaseStorage.child("Photo/"+System.currentTimeMillis()+"cover_image.jpg")
        //Upload image to database.
        storage.putFile(imageUri).addOnCompleteListener { uploadImage ->
            if( uploadImage.isSuccessful){
                // Download image from database.
                storage.downloadUrl.addOnSuccessListener { imageUrl ->
                    var map = HashMap<String , Any>()
                    map[Constants.USER_COVER] = imageUrl.toString()
                    userReference.child(Constants.getCurrentUser()).updateChildren(map)
                    Constants.hideProgressDialog()
                    btn_save_change.visibility = View.GONE
                }
            }else{
                Constants.hideProgressDialog()
            }
        }

    }

    // function operation work for upload profile image
    fun uploadProfileImageToDatabase( imageUri : Uri,
                                      btn_save_change : Button){
        var storage : StorageReference = firebaseStorage.child("Photo/"+System.currentTimeMillis()+"profile_image.jpg")
        //Upload image to data base.
        storage.putFile(imageUri).addOnCompleteListener { uploadImage ->
            if( uploadImage.isSuccessful){
                // Download image from database.
                storage.downloadUrl.addOnSuccessListener { imageUrl ->
                    var map = HashMap<String , Any>()
                    map[Constants.USER_PROFILE] = imageUrl.toString()
                    userReference.child(Constants.getCurrentUser()).updateChildren(map)
                    Constants.hideProgressDialog()
                    btn_save_change.visibility = View.GONE
                }
            }else{
                Constants.hideProgressDialog()
            }
        }
    }


    // Function get data for user profile from database
    fun getUserDetails( context                 : Context,
                        tv_username_settings    : TextView,
                        iv_profile_settings     : ImageView,
                        iv_cover_settings       : ImageView){

        userReference.child(Constants.getCurrentUser()).addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                if(snapshot.exists()){
                    var user = snapshot.getValue(UsersModel::class.java)!!
                    tv_username_settings.text = user.username
                    Picasso.get().load(user.cover).into(iv_cover_settings)
                    Picasso.get().load(user.profile).into(iv_profile_settings)
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context , error.message ,Toast.LENGTH_SHORT).show()
            }
        })
    }

    // fun write social media link in alertDialog
    fun setSocialLink( context : Context, socialChecker : String){

        var builderDialog = AlertDialog.Builder(context)
        if( socialChecker == Constants.USER_WEBSITE){
            builderDialog.setTitle("Write URL:")
        }else{
            builderDialog.setTitle("Write username")
        }
        val editText = EditText(context)
        if(socialChecker == Constants.USER_WEBSITE){
            editText.hint = Constants.HINT_WEB_SITE
        }else{
            editText.hint = Constants.HINT_SOCIAL_NAME
        }
        builderDialog.setView(editText)
        builderDialog.setPositiveButton("create"){dialog,witch->

            val string = editText.text.toString()
            if(string == ""){
                Toast.makeText(context, "Please write something...", Toast.LENGTH_LONG).show()
            }else{
                // Call save social link in database
                saveSocialLink(socialChecker , string)
            }
        }
        builderDialog.setNegativeButton("cancel" , null)
        builderDialog.create().show()
    }

    // fun save social link in database
    private fun saveSocialLink(socialChecker : String , string : String){
        var map = HashMap<String , Any>()

        when(socialChecker){
            Constants.USER_FACEBOOK ->{
                map[Constants.USER_FACEBOOK] = "https://facebook.com/$string"
            }
            Constants.USER_INSTAGRAM ->{
                map[Constants.USER_INSTAGRAM] = "https://instagram.com/$string"
            }
            Constants.USER_WEBSITE ->{
                map[Constants.USER_WEBSITE] = "https://$string"
            }
        }
        userReference.child(Constants.getCurrentUser()).updateChildren(map)
    }
}