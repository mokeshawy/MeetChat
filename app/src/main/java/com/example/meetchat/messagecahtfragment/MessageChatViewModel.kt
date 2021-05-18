package com.example.meetchat.messagecahtfragment

import android.content.Context
import android.util.Log
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.meetchat.model.ChatModel
import com.example.meetchat.util.Constants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class MessageChatViewModel : ViewModel() {

    var etSendMessage = MutableLiveData<String>("")

    var mChatArrayListLive      = MutableLiveData<ArrayList<ChatModel>>()
    var mChatArrayList          = ArrayList<ChatModel>()

    // variable for seen message
    var seenListener    : ValueEventListener? = null


    // Firebase instance
    var firebaseDatabase        = FirebaseDatabase.getInstance()
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
            sendMessageToUser( context , senderId , receiverId , etSendMessage.value!!)
            etSendMessage.value = ""
        }
    }

    //function send message to user
    private fun sendMessageToUser( context: Context,
                                   senderId : String,
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
}