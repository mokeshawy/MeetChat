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
    const val DEFAULT_IMAGE_PROFILE = "https://firebasestorage.googleapis.com/v0/b/meetchat-8e945.appspot.com/o/Photo%2Fic_profile_image.png?alt=media&token=8abff52c-5b67-4e88-b1fb-2812ce7b24b0"
    const val DEFAULT_COVER_IMAGE   = "https://firebasestorage.googleapis.com/v0/b/meetchat-8e945.appspot.com/o/Photo%2Fdefualt_cover_profile.png?alt=media&token=4a90b9c5-53a8-4073-982a-01f508a3e62a"
    const val DEFAULT_FACEBOOK_URL  = "https://www.facebook.com/"
    const val DEFAULT_INSTA_URL     = "https://www.instagram.com/"
    const val DEFAULT_WEB_URL       = "https://www.google.com"


    //Serializable Key
    const val SERIALIZABLE_USERS = "Users"
    const val SERIALIZABLE_USERS_PROFILE = "UserProfile"
    const val SERIALIZABLE_USERS_PROFILE_SHOW_FULL_IMAGE = "FullImageProfile"
    const val SERIALIZABLE_USERS_PROFILE_SHOW_FULL_COVER = "FullImageCover"

    // pic image key
    const val PICK_COVER_IMAGE_REQUEST      = 10
    const val PICK_PROFILE_IMAGE_REQUEST    = 11
    const val PICK_ATTACH_IMAGE_REQUEST     = 11

    // hint for alert dialog
    const val HINT_WEB_SITE = "e.g www.google.com"
    const val HINT_SOCIAL_NAME = "e.g MohamedKeshawy"


    // Reference for Chats in real time database
    const val CHATS_REFERENCE = "Chats"

    // Child Chats reference in real time database
    const val CHATS_SENDER      = "sender"
    const val CHATS_MESSAGE     = "message"
    const val CHATS_RECEIVER    = "receiver"
    const val CHATS_IS_SEEN     = "seen"
    const val CHATS_URL         = "url"
    const val CHATS_MESSAGE_ID  = "messageId"

    // Users Adapter const key
    const val VISIT_ID = "visit_id"

    // ChatList Reference
    const val CHAT_LIST_REFERENCE   = "ChatList"
    const val SENT_YOU_IMAGE        = "sent you an image."

    // Child ChatList reference
    const val CHAT_LIST_ID = "id"


    // view full image page key
    const val FULL_IMAGE_VIEW = "imageUrl"


    // fun get id for user login
    fun getCurrentUser() : String{
        var firebaseAuth    = FirebaseAuth.getInstance().currentUser
        var currentUser     = ""
        if( currentUser !=null){
            currentUser = firebaseAuth.uid
        }
        return currentUser
    }


    // Progress dialog.
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

    // hide progress bar.
    fun hideProgressDialog(){
        mProgressDialog.dismiss()
    }
}