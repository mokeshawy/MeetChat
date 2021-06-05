package com.example.meetchat.viewpagerfragment

import android.os.Bundle
import android.util.Log
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.meetchat.R
import com.example.meetchat.databinding.FragmentViewPagerBinding
import com.example.meetchat.model.UsersModel
import com.example.meetchat.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.messaging.Constants.TAG
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async


class ViewPagerFragment : Fragment() {

    lateinit var binding            : FragmentViewPagerBinding
    private val viewPagerViewModel  : ViewPagerViewModel by viewModels()
    private var user                : UsersModel? = null


    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        binding = FragmentViewPagerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        if( arguments?.containsKey(Constants.SERIALIZABLE_USERS) == true){
            // Show data for profile user login
            user = arguments?.getSerializable(Constants.SERIALIZABLE_USERS) as UsersModel
            Picasso.get().load(user!!.profile).into(binding.ivProfileImage)
            binding.tvUserName.text = user!!.username
        }


        // connect with view model for viewPager
        binding.lifecycleOwner      = this
        binding.viewPagerVarModel   = viewPagerViewModel


        // operation viewPager with tabLayout.
        viewPagerViewModel.viewPagerWork( requireActivity(),
            binding.viewPager,
            binding.tabLayout,
            requireActivity().supportFragmentManager,
            lifecycle)


        // set menu with toolbar
        binding.toolBarViewPager.inflateMenu(R.menu.menu_view_pager)
        //Handling click events on menu
        binding.toolBarViewPager.setOnMenuItemClickListener {
            when (it.itemId) {
                R.id.action_sign_out -> {

                    var alert = AlertDialog.Builder(requireActivity())
                    alert.setTitle("Are you sure Sign out ?")
                    alert.setMessage("After click 'yes' will go Sign out.")
                    alert.setPositiveButton("yes"){dialog,which ->
                        FirebaseAuth.getInstance().signOut()
                        Navigation.findNavController(view).navigate(R.id.action_viewPagerFragment_to_loginFragment)
                    }
                    alert.setNegativeButton("no",null)
                    alert.create().show()

                    // when user go sign out change status to offline
                    CoroutineScope(Dispatchers.IO).async {
                        Constants.updateStatus("Offline")
                    }
                    true
                }
                R.id.action_delete_account ->{

                var alert = AlertDialog.Builder(requireActivity())
                alert.setTitle("Are you sure delete your account?")
                alert.setMessage("After click 'yes' will go delete your account.")
                alert.setPositiveButton("yes"){dialog,which ->
                    val userReference = FirebaseDatabase.getInstance().getReference(Constants.USER_REFERENCE)
                    FirebaseAuth.getInstance().currentUser.delete()
                        .addOnCompleteListener { task ->
                            if (task.isSuccessful) {

                            }
                        }
                    // when go delete account will delete all details for user.
                    userReference.child(Constants.getCurrentUser()).removeValue()
                    Toast.makeText(requireActivity() , "Your account has bin deleted", Toast.LENGTH_SHORT).show()
                    Navigation.findNavController(view).navigate(R.id.action_viewPagerFragment_to_loginFragment)

                }
                alert.setNegativeButton("no",null)
                alert.create().show()
                    true
                }
                else -> false
            }
        }
        binding.ivProfileImage.setOnClickListener {
            var bundle = bundleOf(Constants.FULL_IMAGE_VIEW to user!!.profile)
            findNavController().navigate(R.id.action_viewPagerFragment_to_viewFullImageFragment, bundle)
        }
    }
}