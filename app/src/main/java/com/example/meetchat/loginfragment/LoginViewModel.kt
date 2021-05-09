package com.example.meetchat.loginfragment

import android.content.Context
import android.view.View
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.meetchat.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {

    var etEnterEmail    = MutableLiveData<String>("")
    var etEnterPassword = MutableLiveData<String>("")

    var firebaseAuth = FirebaseAuth.getInstance()

    // fun log in
    fun login( context: Context , view : View){

        // validate input for login page
        if( etEnterEmail.value!!.trim().isEmpty()){
            Snackbar.make(view,context.resources.getString(R.string.err_msg_please_enter_your_email),Snackbar.LENGTH_SHORT).show()
        }else if( etEnterPassword.value!!.trim().isEmpty()){
            Snackbar.make(view,context.resources.getString(R.string.err_msg_please_enter_your_password),Snackbar.LENGTH_SHORT).show()
        }else if( etEnterPassword.value!!.length < 6){
            Snackbar.make(view,context.resources.getString(R.string.err_msg_the_password_is_not_less_than),Snackbar.LENGTH_SHORT).show()
        }else{
            firebaseAuth.signInWithEmailAndPassword( etEnterEmail.value!! , etEnterPassword.value!!).addOnCompleteListener {
                if(it.isSuccessful){
                    if(firebaseAuth.currentUser?.isEmailVerified!!){
                        Snackbar.make(view ,context.resources.getString(R.string.msg_welcome_user_login), Snackbar.LENGTH_SHORT).show()
                        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_viewPagerFragment)
                    }else{
                        Snackbar.make(view ,context.resources.getString(R.string.err_msg_confirm_email), Snackbar.LENGTH_SHORT).show()
                    }
                }else{
                    Snackbar.make(view , it.exception!!.message.toString(), Snackbar.LENGTH_SHORT).show()
                }
            }
        }
    }

    // fun go register page
    fun goRegisterPage( view: View , tv_register_new_account : TextView){
        tv_register_new_account.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment)
        }
    }

    // fun go to page forget password
    fun goForgetPasswordPage( view : View , tv_forget_password : TextView){
        tv_forget_password.setOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
        }
    }
}