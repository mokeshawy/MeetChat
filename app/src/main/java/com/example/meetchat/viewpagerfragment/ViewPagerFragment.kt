package com.example.meetchat.viewpagerfragment

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import com.example.meetchat.R
import com.example.meetchat.activitis.MainActivity
import com.example.meetchat.databinding.FragmentViewPagerBinding
import com.example.meetchat.model.UsersModel
import com.example.meetchat.searchfragment.SearchFragment
import com.example.meetchat.util.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class ViewPagerFragment : Fragment() {

    lateinit var binding : FragmentViewPagerBinding
    private val viewPagerViewModel : ViewPagerViewModel by viewModels()


    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        binding = FragmentViewPagerBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)


        // Show data for profile user login
        var user = arguments?.getSerializable(Constants.SERIALIZABLE_USERS) as UsersModel
        Picasso.get().load(user.profile).into(binding.ivProfileImage)
        binding.tvUserName.text = user.username


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
                R.id.action_setting -> {
                    var userReference = FirebaseDatabase.getInstance().getReference(Constants.USER_REFERENCE)

                    var map = HashMap<String , Any>()
                    map[Constants.USER_STATUS] = "Offline"
                    userReference.child(Constants.getCurrentUser()).updateChildren(map)

                    Navigation.findNavController(view).navigate(R.id.action_viewPagerFragment_to_loginFragment)
                    true
                }
                else -> false
            }

        }
    }
}