package com.example.meetchat.loginfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.meetchat.R
import com.example.meetchat.databinding.FragmentLoginBinding
import com.example.meetchat.model.UsersModel
import com.example.meetchat.util.Constants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class LoginFragment : Fragment() {

    lateinit var binding        : FragmentLoginBinding
    private val loginViewModel  : LoginViewModel by viewModels()

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        binding = FragmentLoginBinding.inflate(inflater)
        return binding.root
    }



    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Connect with Login view model
        binding.lifecycleOwner  = this
        binding.loginVarModel   = loginViewModel



        // button for login
        binding.btnLogIn.setOnClickListener {
            loginViewModel.login(requireActivity() , view)

        }

        binding.tvRegisterNewAccount.setOnClickListener {
            // call function for go register page
            loginViewModel.goRegisterPage(view )
        }

        binding.tvForgetPassword.setOnClickListener {
            // call function for go to forget password page
            loginViewModel.goForgetPasswordPage(view)
        }

    }
}