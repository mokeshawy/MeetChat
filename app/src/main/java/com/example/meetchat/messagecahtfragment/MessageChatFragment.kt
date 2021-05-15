package com.example.meetchat.messagecahtfragment

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
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import com.example.meetchat.`interface`.OnClickChatAdapter
import com.example.meetchat.adapter.RecyclerChatAdapter
import com.example.meetchat.databinding.FragmentMessageChatBinding
import com.example.meetchat.model.ChatModel
import com.example.meetchat.model.UsersModel
import com.example.meetchat.util.Constants
import com.google.firebase.database.FirebaseDatabase
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

        // receive arguments from users adapter for information user
         userVisit = arguments?.getSerializable(Constants.VISIT_ID) as UsersModel

        // Show details for user Visit in tool bar
        Picasso.get().load(userVisit.profile).into(binding.ivProfileImageChat)
        binding.tvUserNameChat.text = userVisit.username

        binding.ivSendMessageBtn.setOnClickListener {
            // fun send message
            messageChatViewModel.sendMessage(requireActivity() , Constants.getCurrentUser() , userVisit.uid)
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

        // call function retrieve message
        messageChatViewModel.retrieveMessage(Constants.getCurrentUser() , userVisit.uid)
        messageChatViewModel.mChatArrayListLive.observe(viewLifecycleOwner, Observer {
            binding.recyclerViewChats.adapter = RecyclerChatAdapter(it ,userVisit.profile,this)
        })
    }


    @Suppress("DEPRECATION")
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        var chatReference   = firebaseDatabase.getReference(Constants.CHATS_REFERENCE)
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
                    }
                    loadingBar.dismiss()
                }else{
                    Toast.makeText(requireActivity(),uploadFile.exception!!.message.toString(),Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    // Onclick interface from chat adapter
    override fun onClickMessageItem( viewHolder: RecyclerChatAdapter.ViewHolder,
                                     mChatList: ArrayList<ChatModel> ,
                                     dataSet: ChatModel,
                                     position: Int) {

        // images messages
        if( dataSet.message == Constants.SENT_YOU_IMAGE && dataSet.url != ""){

            // image message - right side
            if( dataSet.sender == Constants.getCurrentUser()){

                viewHolder.showTextMessage.visibility = View.GONE
                viewHolder.rightImageView!!.visibility = View.VISIBLE
                Picasso.get().load(dataSet.url).into(viewHolder.rightImageView)

            }else if(dataSet.sender != Constants.getCurrentUser()){ //=> image message - left side

                viewHolder.showTextMessage.visibility = View.GONE
                viewHolder.leftImageView!!.visibility = View.VISIBLE
                Picasso.get().load(dataSet.url).into(viewHolder.leftImageView)

            }

        }else{  //=> text message
            viewHolder.showTextMessage.text = dataSet.message
        }

        // sent and seen message..
        if(position == mChatList.size -1){  // Seen.
            if(dataSet.isSeen){
                viewHolder.textSeen.text = "Seen"
                if(dataSet.message == "sent you an image." && dataSet.url != "" ){

                    val lp : RelativeLayout.LayoutParams = viewHolder.textSeen.layoutParams as RelativeLayout.LayoutParams
                    lp.setMargins(0 ,245 , 10 , 0)
                    viewHolder.textSeen.layoutParams = lp
                }
            }else{  // Sent.
                viewHolder.textSeen.text = "Sent"
                if(dataSet.message == "sent you an image." && dataSet.url != "" ){

                    val lp : RelativeLayout.LayoutParams = viewHolder.textSeen.layoutParams as RelativeLayout.LayoutParams
                    lp.setMargins(0 ,245 , 10 , 0)
                    viewHolder.textSeen.layoutParams = lp
                }
            }
        }else{
            viewHolder.textSeen.visibility = View.GONE
        }
    }
}