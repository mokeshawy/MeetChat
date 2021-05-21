package com.example.meetchat.registerfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.meetchat.R
import com.example.meetchat.databinding.FragmentRegisterBinding
import com.example.meetchat.registerfragment.RegisterViewModel

class RegisterFragment : Fragment() {

    lateinit var binding : FragmentRegisterBinding
    private val registerViewModel : RegisterViewModel by viewModels()

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        binding = FragmentRegisterBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // connect with register view model.
        binding.lifecycleOwner  = this
        binding.registerVarMdel = registerViewModel


       //click button for create new account
        binding.btnRegister.setOnClickListener {
            // call function register new account
            registerViewModel.registerAccount( requireActivity() , view)
        }

        binding.tvLogin.setOnClickListener {
            // call function go login page
            registerViewModel.goLoginPage(view)
        }


        // toolbar for click back to login page.
        binding.toolBarRegister.setNavigationIcon(R.drawable.ic_vector_arrow_back)
        binding.toolBarRegister.setNavigationOnClickListener {
            Navigation.findNavController(view).navigate(R.id.action_registerFragment_to_loginFragment)
        }
    }
}