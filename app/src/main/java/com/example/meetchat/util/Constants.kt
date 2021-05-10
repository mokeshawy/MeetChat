package com.example.meetchat.util

import android.app.Dialog
import android.content.Context
import android.widget.TextView
import com.example.meetchat.R
import com.google.firebase.auth.FirebaseAuth

object Constants {

    // Reference for user in real time database
    const val USER_REFERENCE = "Users"

    // Child user in real time database
    const val USER_ID           = "uid"
    const val USER_NAME         = "username"
    const val USER_PROFILE      = "profile"
    const val USER_COVER        = "cover"
    const val USER_STATUS       = "status"
    const val USER_SEARCH       = "search"
    const val USER_FACEBOOK     = "facebook"
    const val USER_INSTAGRAM    = "instagram"
    const val USER_WEBSITE      = "website"


    // default image for profile and cover
    const val DEFAULT_IMAGE_PROFILE = "https://firebasestorage.googleapis.com/v0/b/meetchat-8e945.appspot.com/o/Photo%2Fic_profile_image.png?alt=media&token=185f6fea-8e6e-4e34-93a5-af33b9792f64"
    const val DEFAULT_COVER_IMAGE   = "https://firebasestorage.googleapis.com/v0/b/meetchat-8e945.appspot.com/o/Photo%2Fdefualt_cover_profile.png?alt=media&token=4a90b9c5-53a8-4073-982a-01f508a3e62a"
    const val DEFAULT_FACEBOOK_URL  = "https://www.facebook.com/"
    const val DEFAULT_INSTA_URL     = "https://www.instagram.com/"
    const val DEFAULT_WEB_URL       = "https://www.google.com"


    //Serializable Key
    const val SERIALIZABLE_USERS = "Users"

    // pic image key
    const val PICK_COVER_IMAGE_REQUEST = 10
    const val PICK_PROFILE_IMAGE_REQUEST = 11

    // fun get id for user login
    fun getCurrentUser() : String{
        var firebaseAuth = FirebaseAuth.getInstance().currentUser
        var currentUser = ""
        if( currentUser !=null){
            currentUser = firebaseAuth.uid
        }
        return currentUser
    }


    lateinit var mProgressDialog : Dialog

    fun showProgressDialog( text : String ,context: Context){

        mProgressDialog = Dialog(context)

        /* Set the screen content from a layout resource
        The resource will be inflated , adding all top-level views to the screen */
        mProgressDialog.setContentView(R.layout.dialog_progress)
        var dialog = mProgressDialog.findViewById(R.id.tv_progress_textId) as TextView
        dialog.text = text

        mProgressDialog.setCancelable(false)
        mProgressDialog.setCanceledOnTouchOutside(false)

        mProgressDialog.show()

    }

    // hide progress bar
    fun hideProgressDialog(){
        mProgressDialog.dismiss()
    }
}