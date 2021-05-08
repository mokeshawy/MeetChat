package com.example.meetchat.registerfragment

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.meetchat.R
import com.google.android.material.snackbar.Snackbar

class RegisterViewModel : ViewModel(){

    var etEnterName             = MutableLiveData<String>("")
    var etEnterEmail            = MutableLiveData<String>("")
    var etEnterPassword         = MutableLiveData<String>("")
    var etEnterConfirmPassword  = MutableLiveData<String>("")


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


        }
    }


    // fun got log in page
    fun goLoginPage( view: View , tv_login : TextView){
        tv_login.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }


    // back to login page by toolbar icon back
    fun backToLoginPage(view: View, toolbar: androidx.appcompat.widget.Toolbar){
        // set icon on action bar for back to login fragment
        toolbar.setNavigationIcon(R.drawable.ic_vector_arrow_back)
        toolbar.setNavigationOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }
}