package com.example.meetchat.chatsfragment

import android.app.AlertDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.meetchat.R
import com.example.meetchat.`interface`.OnClickChatListAdapter
import com.example.meetchat.adapter.RecyclerChatListAdapter
import com.example.meetchat.databinding.FragmentChatsBinding
import com.example.meetchat.model.UsersModel
import com.example.meetchat.util.Constants
import com.google.firebase.iid.FirebaseInstanceId

class ChatsFragment : Fragment() , OnClickChatListAdapter{

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

        // call get chat list function
        chatsViewModel.getChatList(requireActivity(),binding.recyclerViewChatList,binding.tvNoMessage)
        // show chat fro user login in chat fragment
        chatsViewModel.mUsersLiveData.observe(viewLifecycleOwner , Observer {
            binding.recyclerViewChatList.adapter = RecyclerChatListAdapter(it , this)
        })

    }

    override fun onClickChatListAdapter(
        viewHolder: RecyclerChatListAdapter.ViewHolder,
        dataSet: UsersModel,
        position: Int , isChecked : Boolean) {
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

        // Get last message.
        if(isChecked){
            chatsViewModel.retrieveLastMessage(requireActivity() ,
                dataSet.uid ,
                viewHolder.binding.tvLastMessage)

        }else{
            viewHolder.binding.tvLastMessage.visibility = View.GONE
        }


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