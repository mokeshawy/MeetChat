package com.example.meetchat.forgetpassowrd

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.example.meetchat.databinding.FragmentForgetPasswordBinding

class ForgetPasswordFragment : Fragment() {

    lateinit var binding: FragmentForgetPasswordBinding
    private val forgetPasswordViewModel : ForgetPasswordViewModel by viewModels()

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        binding = FragmentForgetPasswordBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // connect with view model
        binding.lifecycleOwner = this
        binding.forgetPasswordVarModel = forgetPasswordViewModel



        // call function for back to login page
        forgetPasswordViewModel.backToLoginPage(view , binding.toolBarForgetPassword)
    }
}