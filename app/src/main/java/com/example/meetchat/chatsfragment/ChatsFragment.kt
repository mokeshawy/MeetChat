package com.example.meetchat.chatsfragment

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.meetchat.`interface`.OnClickUsersAdapter
import com.example.meetchat.adapter.RecyclerUsersAdapter
import com.example.meetchat.databinding.FragmentChatsBinding
import com.example.meetchat.viewpagerfragment.ViewPagerFragment

class ChatsFragment : Fragment(){

    lateinit var binding : FragmentChatsBinding
    private val chatsViewModel : ChatsViewModel by viewModels()

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        binding = FragmentChatsBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // connect with chats view model
        binding.lifecycleOwner  = this
        binding.chatsVarModel   = chatsViewModel

//        var viewPagerFragment = ViewPagerFragment()
//        chatsViewModel.getChatList(requireActivity(),binding.recyclerViewChatList,binding.tvNoMessage)
//        chatsViewModel.mUsersLiveData.observe(viewLifecycleOwner, Observer {
//            binding.recyclerViewChatList.adapter = RecyclerUsersAdapter(it , requireActivity() , viewPagerFragment )
//        })
    }
}