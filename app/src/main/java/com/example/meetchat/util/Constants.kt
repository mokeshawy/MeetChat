package com.example.meetchat.util

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
    const val DEFAULT_IMAGE_PROFILE = "https://firebasestorage.googleapis.com/v0/b/meetchat-8e945.appspot.com/o/Photo%2Fic_profile_image.png?alt=media&token=513cd23f-963a-4054-ac43-3ab424e67d7b"
    const val DEFAULT_COVER_IMAGE   = "https://firebasestorage.googleapis.com/v0/b/meetchat-8e945.appspot.com/o/Photo%2Fcover_for_profile_user.jpg?alt=media&token=bc29d58d-2f7a-40cb-ad2f-0164cf983ee4"
    const val DEFAULT_FACEBOOK_URL  = "https://www.facebook.com/"
    const val DEFAULT_INSTA_URL     = "https://www.instagram.com/"
    const val DEFAULT_WEB_URL       = "https://www.google.com"


    //Serializable Key
    const val SERIALIZABLE_USERS = "Users"




    // fun get id for user login
    fun getCurrentUser() : String{
        var firebaseAuth = FirebaseAuth.getInstance().currentUser
        var currentUser = ""
        if( currentUser !=null){
            currentUser = firebaseAuth.uid
        }
        return currentUser
    }
}