package com.example.meetchat.searchfragment

import android.app.AlertDialog
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.example.meetchat.R
import com.example.meetchat.`interface`.OnClickUsersAdapter
import com.example.meetchat.adapter.RecyclerUsersAdapter
import com.example.meetchat.databinding.FragmentSearchBinding
import com.example.meetchat.model.UsersModel
import com.example.meetchat.util.Constants
import java.util.*

class SearchFragment : Fragment() , OnClickUsersAdapter {

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
        searchViewModel.retrieveAllUsers(requireActivity())
        searchViewModel.mUsersAdapterLiveData.observe(viewLifecycleOwner , androidx.lifecycle.Observer {
            binding.rvSearchList.setHasFixedSize(true)
            binding.rvSearchList.adapter = RecyclerUsersAdapter(it  ,this )
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
                    binding.rvSearchList.adapter = RecyclerUsersAdapter(it , this@SearchFragment)
                })
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    // OnClick for RecyclerUserAdapter using interface
    override fun onClickUsersAdapter(
        viewHolder: RecyclerUsersAdapter.ViewHolder,
        dataSet: UsersModel,
        position: Int,
        isChecked: Boolean ) {

        // set click listener on item view
        viewHolder.itemView.setOnClickListener {
            val options = arrayOf<CharSequence>(
                "Send Message",
                "Visit Profile"
            )
            val builder = AlertDialog.Builder(requireActivity())
            builder.setTitle("What do you want?")
            builder.setItems(options){dialog,position->
                if( position == 0){
                    var bundle = Bundle()
                    bundle.putSerializable(Constants.VISIT_ID , dataSet)
                   findNavController().navigate(R.id.action_viewPagerFragment_to_messageChatFragment , bundle)
                }
                if( position == 1){

                }
            }
            builder.setNegativeButton("cancel",null)
            builder.create().show()
        }


        // update status
        if(isChecked){
            if( dataSet.status == "Online"){
                viewHolder.binding.ivImageOnline.visibility = View.VISIBLE
                viewHolder.binding.ivImageOffline.visibility = View.GONE
            }else{
                viewHolder.binding.ivImageOnline.visibility = View.GONE
                viewHolder.binding.ivImageOffline.visibility = View.VISIBLE
            }
        }else{
            viewHolder.binding.ivImageOnline.visibility = View.GONE
            viewHolder.binding.ivImageOffline.visibility = View.GONE
        }
    }
}