package com.example.meetchat.registerfragment

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.meetchat.R
import com.example.meetchat.util.Constants
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import java.util.*
import kotlin.collections.HashMap

class RegisterViewModel : ViewModel(){

    var etEnterName             = MutableLiveData<String>("")
    var etEnterEmail            = MutableLiveData<String>("")
    var etEnterPassword         = MutableLiveData<String>("")
    var etEnterConfirmPassword  = MutableLiveData<String>("")

    // Firebase instance
    var firebaseAuth        = FirebaseAuth.getInstance()
    var firebaseDatabase    = FirebaseDatabase.getInstance()
    var userReference       = firebaseDatabase.getReference(Constants.USER_REFERENCE)

    // fun create new account
    fun registerAccount( context : Context , view : View){

        // validate input for register page
        if(etEnterName.value!!.trim().isEmpty()){
           Snackbar.make(view , context.resources.getString(R.string.err_msg_please_enter_your_name), Snackbar.LENGTH_SHORT).show()
        }else if( etEnterEmail.value!!.trim().isEmpty()){
            Snackbar.make(view , context.resources.getString(R.string.err_msg_please_enter_your_email), Snackbar.LENGTH_SHORT).show()
        }else if( etEnterPassword.value!!.trim().isEmpty()){
            Snackbar.make(view , context.resources.getString(R.string.err_msg_please_enter_your_password), Snackbar.LENGTH_SHORT).show()
        }else if( etEnterPassword.value!!.length < 6){
            Snackbar.make(view , context.resources.getString(R.string.err_msg_the_password_is_not_less_than), Snackbar.LENGTH_SHORT).show()
        }else if( etEnterConfirmPassword.value!!.trim().isEmpty()){
            Snackbar.make(view , context.resources.getString(R.string.err_msg_please_enter_confirm_password), Snackbar.LENGTH_SHORT).show()
        }else if(etEnterPassword.value!! != etEnterConfirmPassword.value!!){
            Snackbar.make(view , context.resources.getString(R.string.err_msg_password_and_confirm_password_not_mismatch), Snackbar.LENGTH_SHORT).show()
        }else{
            Constants.showProgressDialog(context.resources.getString(R.string.please_wait) ,context)
            firebaseAuth.createUserWithEmailAndPassword( etEnterEmail.value!! , etEnterPassword.value!!)
                .addOnCompleteListener {
                    if(it.isSuccessful){
                        firebaseAuth.currentUser?.sendEmailVerification()
                        var uid = firebaseAuth.currentUser?.uid

                        // make insert data for user to realtime database use HashMap
                        var map = HashMap<String , Any>()

                        map[Constants.USER_ID]          = uid.toString()
                        map[Constants.USER_NAME]        = etEnterName.value!!
                        map[Constants.USER_PROFILE]     = Constants.DEFAULT_IMAGE_PROFILE
                        map[Constants.USER_COVER]       = Constants.DEFAULT_COVER_IMAGE
                        map[Constants.USER_STATUS]      = "offline"
                        map[Constants.USER_SEARCH]      = etEnterName.value!!.lowercase(Locale.getDefault())
                        map[Constants.USER_FACEBOOK]    = Constants.DEFAULT_FACEBOOK_URL
                        map[Constants.USER_INSTAGRAM]   = Constants.DEFAULT_INSTA_URL
                        map[Constants.USER_WEBSITE]     = Constants.DEFAULT_WEB_URL

                        userReference.child(uid.toString()).setValue(map).addOnCompleteListener { setValue ->
                            if(setValue.isSuccessful){
                                Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)
                                Constants.hideProgressDialog()
                            }else{
                                Snackbar.make(view , setValue.exception!!.message.toString(), Snackbar.LENGTH_SHORT).show()
                                Constants.hideProgressDialog()
                            }
                        }
                    }else{
                        Snackbar.make(view , it.exception!!.message.toString(), Snackbar.LENGTH_SHORT).show()
                        Constants.hideProgressDialog()
                    }
            }
        }
    }


    // fun got log in page
    fun goLoginPage( view: View){
        Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)
    }

    // back to login page by toolbar icon back
    fun backToLoginPage(view: View, toolbar: androidx.appcompat.widget.Toolbar){
        // set icon on action bar for back to login page
        toolbar.setNavigationIcon(R.drawable.ic_vector_arrow_back)
        toolbar.setNavigationOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }
}