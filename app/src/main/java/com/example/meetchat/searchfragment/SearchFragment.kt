package com.example.meetchat.searchfragment

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import com.example.meetchat.R
import com.example.meetchat.adapter.RecyclerUsersAdapter
import com.example.meetchat.databinding.FragmentSearchBinding
import com.example.meetchat.model.UsersModel
import com.example.meetchat.util.Constants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class SearchFragment : Fragment() {

    lateinit var binding : FragmentSearchBinding
    private val searchViewModel : SearchViewModel by viewModels()

    private lateinit var mUsers : ArrayList<UsersModel>

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


        retrieveAllUsers()

    }


    fun retrieveAllUsers(){
        mUsers = ArrayList()
        var userReference = FirebaseDatabase.getInstance().getReference(Constants.USER_REFERENCE)

        userReference.addValueEventListener( object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                mUsers.clear()
                for (ds in snapshot.children){

                    var users = ds.getValue(UsersModel::class.java)!!
                    Log.e("User ${users.username}", "Done")
                    if(users.uid != Constants.getCurrentUser()){
                        mUsers.add(users)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(requireActivity() , error.message , Toast.LENGTH_SHORT).show()
            }
        })
    }
}