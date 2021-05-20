package com.example.meetchat.forgetpassowrd

import android.view.View
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import com.example.meetchat.R

class ForgetPasswordViewModel : ViewModel(){


    // back to login page by toolbar icon back
    fun backToLoginPage(view: View, toolbar: androidx.appcompat.widget.Toolbar){
        // set icon on action bar for back to login fragment
        toolbar.setNavigationIcon(R.drawable.ic_vector_arrow_back)
        toolbar.setNavigationOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_forgetPasswordFragment_to_loginFragment)
        }
    }
}