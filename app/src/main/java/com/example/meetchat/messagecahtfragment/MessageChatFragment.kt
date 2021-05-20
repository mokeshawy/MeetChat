package com.example.meetchat.messagecahtfragment

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.RelativeLayout
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.os.bundleOf
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import com.example.meetchat.R
import com.example.meetchat.`interface`.OnClickChatAdapter
import com.example.meetchat.adapter.RecyclerChatAdapter
import com.example.meetchat.databinding.FragmentMessageChatBinding
import com.example.meetchat.model.ChatModel
import com.example.meetchat.model.UsersModel
import com.example.meetchat.util.Constants
import com.google.firebase.database.*
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import com.squareup.picasso.Picasso

class MessageChatFragment : Fragment() , OnClickChatAdapter{

    lateinit var binding                : FragmentMessageChatBinding
    private val messageChatViewModel    : MessageChatViewModel by viewModels()
    lateinit var fileUri                : Uri
    lateinit var userVisit              : UsersModel
    lateinit var firebaseStorage        : StorageReference
    lateinit var firebaseDatabase       : FirebaseDatabase
    var reference                       : DatabaseReference? = null
    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle? ): View? {
        // Inflate the layout for this fragment
        binding = FragmentMessageChatBinding.inflate(inflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // connect with viewModel
        binding.lifecycleOwner      = this
        binding.messageChatVarModel = messageChatViewModel

        // firebase instance
        firebaseDatabase    = FirebaseDatabase.getInstance()
        firebaseStorage     =  FirebaseStorage.getInstance().reference
        reference           = firebaseDatabase.getReference(Constants.USER_REFERENCE)

        // receive arguments from users adapter for information user
         userVisit = arguments?.getSerializable(Constants.VISIT_ID) as UsersModel

        // show profile image and name for user visit
        Picasso.get().load(userVisit.profile).into(binding.ivProfileImageChat)
        binding.tvUserNameChat.text = userVisit.username

        // iv button send message
        binding.ivSendMessageBtn.setOnClickListener {
            // fun send message
            messageChatViewModel.sendMessage(requireActivity(),
                Constants.getCurrentUser(),
                userVisit.uid)
        }

        //Attach image file
        binding.ivAttachImageFileBtn.setOnClickListener {
            try {
                var intent = Intent(Intent.ACTION_GET_CONTENT)
                intent.type = "image/*"
                startActivityForResult(intent , Constants.PICK_ATTACH_IMAGE_REQUEST)
            }catch (e:Exception){
                Toast.makeText(requireActivity() , e.message.toString(),Toast.LENGTH_LONG).show()
            }
        }

        // Call function retrieveMessage
        messageChatViewModel.retrieveMessage(requireActivity(),
            Constants.getCurrentUser() ,
            userVisit.uid)
        // show chatList in adapter
        messageChatViewModel.mChatArrayListLive.observe(viewLifecycleOwner, Observer {
            binding.recyclerViewChats.adapter = RecyclerChatAdapter(it ,userVisit.profile,this)
        })

        //call seen message function
        messageChatViewModel.seenMessage(requireActivity(),
            userVisit.uid)
    }

    // select photo for send in message
    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var chatReference           = firebaseDatabase.getReference(Constants.CHATS_REFERENCE)
        var chatListReference       = firebaseDatabase.getReference(Constants.CHAT_LIST_REFERENCE)
        var chatsListReceiverRef    = firebaseDatabase.getReference(Constants.CHAT_LIST_REFERENCE)
        var reference = firebaseDatabase.reference

        if( requestCode == Constants.PICK_ATTACH_IMAGE_REQUEST && resultCode == AppCompatActivity.RESULT_OK){

            val loadingBar = ProgressDialog(requireActivity())
            loadingBar.setMessage("Please wait , Image is sending")
            loadingBar.show()
            fileUri = data?.data!!
            var messageId = reference.push().key
            var chatStorage : StorageReference = firebaseStorage.child("ChatImage/$messageId.jpg")
            chatStorage.putFile(fileUri).addOnCompleteListener { uploadFile ->
                if(uploadFile.isSuccessful){
                    chatStorage.downloadUrl.addOnSuccessListener { downloadUri ->
                        // Save image sending in chat reference with information user sender
                        val messageMap = HashMap<String , Any>()
                        messageMap[Constants.CHATS_SENDER]      = Constants.getCurrentUser()
                        messageMap[Constants.CHATS_MESSAGE]     = Constants.SENT_YOU_IMAGE
                        messageMap[Constants.CHATS_RECEIVER]    = userVisit.uid
                        messageMap[Constants.CHATS_IS_SEEN]     = false
                        messageMap[Constants.CHATS_URL]         = downloadUri.toString()
                        messageMap[Constants.CHATS_MESSAGE_ID]  = messageId.toString()

                        chatReference.child(messageId.toString()).setValue(messageMap)
                            .addOnCompleteListener { task ->
                                if(task.isSuccessful){
                                    loadingBar.dismiss()
                                    chatListReference.child(Constants.getCurrentUser()).child(userVisit.uid).child(Constants.CHAT_LIST_ID).setValue(userVisit.uid)
                                    chatsListReceiverRef.child(userVisit.uid).child(Constants.getCurrentUser()).child(Constants.CHAT_LIST_ID).setValue(Constants.getCurrentUser())
                                    }
                                }
                    }

                }else{
                    Toast.makeText(requireActivity(),uploadFile.exception!!.message.toString(),Toast.LENGTH_SHORT).show()
                }
            }
        }
    }


    // OnClick interface from chat adapter
    @SuppressLint("SetTextI18n")
    override fun onClickMessageItem(viewHolder: RecyclerChatAdapter.ViewHolder,
                                    mChatList: ArrayList<ChatModel>,
                                    dataSet: ChatModel,
                                    position: Int) {

        // Call function for operation show image sent and show text message sent and delete image and message from chat.
        messageChatViewModel.showImageAndTextMessage( view ,
            viewHolder,
            dataSet)


        // Call function sent and seen message.
        messageChatViewModel.sentAndSeenMessage(mChatList,
            position,
            dataSet,
            viewHolder.textSeen)

    }


    override fun onPause() {
        super.onPause()
        reference!!.removeEventListener(messageChatViewModel.seenListener!!)
    }

}