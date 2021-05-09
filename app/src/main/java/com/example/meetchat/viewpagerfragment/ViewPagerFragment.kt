package com.example.meetchat.viewpagerfragment

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.meetchat.databinding.FragmentViewPagerBinding
import com.example.meetchat.model.UsersModel
import com.example.meetchat.util.Constants
import com.squareup.picasso.Picasso

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
        viewPagerViewModel.viewPagerWork(
            binding.viewPager,
            binding.tabLayout,
            requireActivity().supportFragmentManager,
            lifecycle)


        // Call function for operation set on menu item
        viewPagerViewModel.setOnMenuItem(
            requireActivity(),
            view,
            binding.toolBarViewPager)
    }
}