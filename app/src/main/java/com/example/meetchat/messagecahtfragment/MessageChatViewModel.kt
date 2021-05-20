package com.example.meetchat.messagecahtfragment

import android.app.AlertDialog
import android.content.Context
import android.view.View
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.core.os.bundleOf
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.example.meetchat.R
import com.example.meetchat.adapter.RecyclerChatAdapter
import com.example.meetchat.model.ChatModel
import com.example.meetchat.util.Constants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.squareup.picasso.Picasso


class MessageChatViewModel : ViewModel() {

    var etSendMessage = MutableLiveData<String>("")

    var mChatArrayListLive      = MutableLiveData<ArrayList<ChatModel>>()
    var mChatArrayList          = ArrayList<ChatModel>()

    // variable for seen message
    var seenListener    : ValueEventListener? = null


    // Firebase instance
    var firebaseDatabase        = FirebaseDatabase.getInstance()
    var usersReference          = firebaseDatabase.getReference(Constants.USER_REFERENCE)
    var chatsReference          = firebaseDatabase.getReference(Constants.CHATS_REFERENCE)
    var chatListReference       = firebaseDatabase.getReference(Constants.CHAT_LIST_REFERENCE)
    var chatsListReceiverRef    = firebaseDatabase.getReference(Constants.CHAT_LIST_REFERENCE)

    //Function send message
    fun sendMessage(context: Context,
                    senderId : String,
                    receiverId : String){

        if( etSendMessage.value!! == ""){
            Toast.makeText(context , "Please write message first ...",Toast.LENGTH_SHORT).show()
        }else{
            // call function send to user
            sendMessageToUser( senderId , receiverId , etSendMessage.value!!)
            etSendMessage.value = ""
        }
    }

    //function send message to user
    private fun sendMessageToUser(  senderId : String,
                                    receiverId : String,
                                    message : String) {
        var messageKey = chatsReference.push().key

        val messageMap = HashMap<String , Any>()

        messageMap[Constants.CHATS_SENDER]      = senderId
        messageMap[Constants.CHATS_MESSAGE]     = message
        messageMap[Constants.CHATS_RECEIVER]    = receiverId
        messageMap[Constants.CHATS_IS_SEEN]     = false
        messageMap[Constants.CHATS_URL]         = ""
        messageMap[Constants.CHATS_MESSAGE_ID]  = messageKey.toString()
        chatsReference.child(messageKey.toString()).setValue(messageMap)
            .addOnCompleteListener { task ->
            if(task.isSuccessful){
                chatListReference.child(senderId).child(receiverId).child(Constants.CHAT_LIST_ID).setValue(receiverId)
                chatsListReceiverRef.child(receiverId).child(senderId).child(Constants.CHAT_LIST_ID).setValue(senderId)
            }
        }
    }




    // function retrieve message
    fun retrieveMessage( context: Context,
                         userSenderId : String,
                         userReceiverId : String){

        mChatArrayList = ArrayList()

        chatsReference.addValueEventListener( object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                mChatArrayList.clear()
                for ( ds in snapshot.children){

                    var chat = ds.getValue(ChatModel::class.java)!!

                    if( chat.receiver == userSenderId && chat.sender == userReceiverId || chat.receiver == userReceiverId && chat.sender == userSenderId){

                        mChatArrayList.add(chat)
                    }
                }
                mChatArrayListLive.value = mChatArrayList
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context , error.message ,Toast.LENGTH_SHORT).show()
            }
        })
    }



    // seen message function.
    fun seenMessage( context: Context,
                     userId : String){

        var chatsReference   = firebaseDatabase.getReference(Constants.CHATS_REFERENCE)
        seenListener = chatsReference.addValueEventListener( object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                for ( ds in snapshot.children){

                    val chat = ds.getValue(ChatModel::class.java)!!

                    if( chat.receiver == Constants.getCurrentUser() && chat.sender == userId){
                        var map = HashMap<String , Any>()

                        map[Constants.CHATS_IS_SEEN] = true

                        ds.ref.updateChildren(map)
                    }
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context , error.message ,Toast.LENGTH_SHORT).show()
            }
        })
    }



    // function for operation show image sent and show text message sent and delete image and message from chat.
    fun showImageAndTextMessage(view: View?,
                                viewHolder: RecyclerChatAdapter.ViewHolder,
                                dataSet: ChatModel ){


        // images messages
        if( dataSet.message == Constants.SENT_YOU_IMAGE && dataSet.url != ""){

            // image message - right side
            if( dataSet.sender == Constants.getCurrentUser()){

                viewHolder.showTextMessage!!.visibility   = View.GONE
                viewHolder.rightImageView!!.visibility  = View.VISIBLE
                Picasso.get().load(dataSet.url).into(viewHolder!!.rightImageView)

                // show full image and delete image
                viewHolder.rightImageView!!.setOnClickListener {
                    val options = arrayOf<CharSequence>(
                        "View Full Image",
                        "Delete Image"
                    )
                    var builder = AlertDialog.Builder(viewHolder.itemView.context)
                    builder.setTitle("What do you want?")
                    builder.setItems(options){dialog,which ->
                        if(which == 0){

                            var bundle = bundleOf(Constants.FULL_IMAGE_VIEW to dataSet.url)
                            Navigation.findNavController(view!!).navigate(R.id.action_messageChatFragment_to_viewFullImageFragment, bundle)

                        }else if(which == 1){
                           deleteSentMessage(dataSet.messageId)
                        }
                    }
                    builder.setNegativeButton("Cancel",null)
                    builder.create().show()
                }

            }else if(dataSet.sender != Constants.getCurrentUser()){ //=> image message - left side

                viewHolder.showTextMessage.visibility = View.GONE
                viewHolder.leftImageView!!.visibility = View.VISIBLE
                Picasso.get().load(dataSet.url).into(viewHolder.leftImageView)

                // show full image and delete image
                viewHolder.leftImageView!!.setOnClickListener {
                    val options = arrayOf<CharSequence>(
                        "View Full Image",
                        "Delete Image"
                    )
                    val builder = AlertDialog.Builder(viewHolder.itemView.context)
                    builder.setTitle("What do you want?")
                    builder.setItems(options){dialog,which ->
                        if(which == 0){

                            val bundle = bundleOf(Constants.FULL_IMAGE_VIEW to dataSet.url)
                            Navigation.findNavController(view!!).navigate(R.id.action_messageChatFragment_to_viewFullImageFragment, bundle)

                        }else if(which == 1){
                            deleteSentMessage(dataSet.messageId)
                        }
                    }
                    builder.setNegativeButton("Cancel",null)
                    builder.create().show()
                }
            }

        }else{  //=> text message
            viewHolder.showTextMessage.text = dataSet.message

            if( Constants.getCurrentUser() == dataSet.sender){
                viewHolder.showTextMessage.setOnClickListener {

                    val options = arrayOf<CharSequence>(
                        "Delete Message"
                    )
                    val builder = AlertDialog.Builder(viewHolder.itemView.context)
                    builder.setTitle("What do you want?")
                    builder.setItems(options){dialog,which ->
                        if(which == 0){
                            deleteSentMessage(dataSet.messageId)
                        }
                    }
                    builder.setNegativeButton("Cancel",null)
                    builder.create().show()
                }
            }
        }
    }


    // function delete sent message.
    private fun deleteSentMessage( messageId : String ){
        chatsReference.child(messageId).removeValue()
    }


    // function sent and seen message.
    fun sentAndSeenMessage( mChatList : ArrayList<ChatModel> , position : Int , dataSet: ChatModel , textSeen : TextView ){
        // sent and seen message..
        if(position == mChatList.size -1){  // Seen.
            if(dataSet.seen){
                textSeen.text = "Seen"
                if(dataSet.message == "sent you an image." && dataSet.url != "" ){

                    val lp : RelativeLayout.LayoutParams = textSeen.layoutParams as RelativeLayout.LayoutParams
                    lp.setMargins(0 ,245 , 10 , 0)
                    textSeen.layoutParams = lp
                }
            }
            else{  // Sent.
                textSeen.text = "Sent"
                if(dataSet.message == "sent you an image." && dataSet.url != "" ){

                    val lp : RelativeLayout.LayoutParams = textSeen.layoutParams as RelativeLayout.LayoutParams
                    lp.setMargins(0 ,245 , 10 , 0)
                    textSeen.layoutParams = lp
                }
            }
        }else{
            textSeen.visibility = View.GONE
        }
    }
}