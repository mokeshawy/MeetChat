package com.example.meetchat.searchfragment

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.RecyclerView
import com.example.meetchat.R
import com.example.meetchat.adapter.RecyclerUsersAdapter
import com.example.meetchat.databinding.FragmentSearchBinding
import com.example.meetchat.model.UsersModel
import com.example.meetchat.util.Constants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.*
import kotlin.collections.ArrayList

class SearchFragment : Fragment() {

    lateinit var binding        : FragmentSearchBinding
    private val searchViewModel : SearchViewModel by viewModels()

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?,savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // connect with search view model
        binding.lifecycleOwner = this
        binding.searchVarModel = searchViewModel


        // Call function for retrieveAllUsers
        searchViewModel.retrieveAllUsers(requireActivity() , binding.etSearchUsers)
        searchViewModel.mUsersAdapterLiveData.observe(viewLifecycleOwner , androidx.lifecycle.Observer {
            binding.rvSearchList.setHasFixedSize(true)
            binding.rvSearchList.adapter = RecyclerUsersAdapter(it,requireActivity(),false)
        })

        // EditText for search
        binding.etSearchUsers.addTextChangedListener( object : TextWatcher{
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // Call function for searchUser
               searchViewModel.searchForUser(requireActivity(),s.toString().lowercase(Locale.getDefault()))
                searchViewModel.mUsersAdapterLiveData.observe(viewLifecycleOwner , androidx.lifecycle.Observer {
                    binding.rvSearchList.setHasFixedSize(true)
                    binding.rvSearchList.adapter = RecyclerUsersAdapter(it,requireActivity(),false)
                })
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
    }
}