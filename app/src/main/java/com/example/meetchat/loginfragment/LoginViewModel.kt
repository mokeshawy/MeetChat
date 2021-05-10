package com.example.meetchat.loginfragment

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.meetchat.R
import com.example.meetchat.model.UsersModel
import com.example.meetchat.util.Constants
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginViewModel : ViewModel() {

    var etEnterEmail    = MutableLiveData<String>("")
    var etEnterPassword = MutableLiveData<String>("")

    // Firebase instance
    var firebaseAuth    = FirebaseAuth.getInstance()
    var firebaseData    = FirebaseDatabase.getInstance()
    var userReference   = firebaseData.getReference(Constants.USER_REFERENCE)

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
            Constants.showProgressDialog(context.resources.getString(R.string.please_wait) ,context)
            firebaseAuth.signInWithEmailAndPassword( etEnterEmail.value!! , etEnterPassword.value!!).addOnCompleteListener {
                if(it.isSuccessful){
                    if(firebaseAuth.currentUser?.isEmailVerified!!){
                        Snackbar.make(view ,context.resources.getString(R.string.msg_welcome_user_login), Snackbar.LENGTH_SHORT).show()
                        if( Constants.getCurrentUser() != null){

                            userReference.orderByChild(Constants.USER_ID).equalTo(Constants.getCurrentUser()).addValueEventListener( object : ValueEventListener{
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    for ( ds in snapshot.children){

                                        var user = ds.getValue(UsersModel::class.java)!!

                                        var bundle = Bundle()
                                        bundle.putSerializable(Constants.SERIALIZABLE_USERS , user)
                                        try{
                                            Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_viewPagerFragment , bundle)
                                            Constants.hideProgressDialog()
                                        }catch(e:Exception){

                                        }

                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText( context , error.message , Toast.LENGTH_SHORT).show()
                                    Constants.hideProgressDialog()
                                }

                            })

                        }
                    }else{
                        Snackbar.make(view ,context.resources.getString(R.string.err_msg_confirm_email), Snackbar.LENGTH_SHORT).show()
                        Constants.hideProgressDialog()
                    }
                }else{
                    Snackbar.make(view , it.exception!!.message.toString(), Snackbar.LENGTH_SHORT).show()
                    Constants.hideProgressDialog()
                }
            }
        }
    }

    // fun go register page
    fun goRegisterPage( view: View){
        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_registerFragment)
    }

    // fun go to page forget password
    fun goForgetPasswordPage( view : View ){
        Navigation.findNavController(view).navigate(R.id.action_loginFragment_to_forgetPasswordFragment)
    }
}