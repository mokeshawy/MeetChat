package com.example.meetchat.chatsfragment

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.meetchat.model.ChatListModel
import com.example.meetchat.model.ChatModel
import com.example.meetchat.model.UsersModel
import com.example.meetchat.util.Constants
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ChatsViewModel : ViewModel() {

    var mUsersLiveData      = MutableLiveData<ArrayList<UsersModel>>()
    var mUsersArray         = ArrayList<UsersModel>()
    var mChatListArray      = ArrayList<ChatListModel>()

    var firebaseDatabase    = FirebaseDatabase.getInstance()
    var usersReference      = firebaseDatabase.getReference(Constants.USER_REFERENCE)
    var chatsReference      = firebaseDatabase.getReference(Constants.CHATS_REFERENCE)
    var chatListReference   = firebaseDatabase.getReference(Constants.CHAT_LIST_REFERENCE)

    var lastMessage : String = ""





    fun getChatList(context: Context , recycler_view_chatList : RecyclerView , tv_no_message : TextView){
        mChatListArray = ArrayList()

        chatListReference.child(Constants.getCurrentUser()).addValueEventListener( object : ValueEventListener{

            override fun onDataChange(snapshot: DataSnapshot) {
                mChatListArray.clear()
                for( ds in snapshot.children){

                    val chatList = ds.getValue(ChatListModel::class.java)!!

                    mChatListArray.add(chatList)
                }
                //call retrieveChat function
                retrieveChatList(context , recycler_view_chatList , tv_no_message)
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context , error.message , Toast.LENGTH_SHORT).show()
            }
        })
    }

    // retrieveChatList from users reference get id equal id from chatList reference
    fun retrieveChatList( context: Context , recycler_view_chatList : RecyclerView , tv_no_message : TextView){
        mUsersArray = ArrayList()
        usersReference.addValueEventListener( object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                mUsersArray.clear()

                for ( ds in snapshot.children){

                    val user = ds.getValue(UsersModel::class.java)!!

                    for (chatList in mChatListArray){

                        if(user.uid == chatList.id){

                            Log.e("User ${chatList.id}","Done")
                            mUsersArray.add(user)
                        }
                    }
                    mUsersLiveData.value = mUsersArray
                }

                if(mUsersArray.size > 0){
                    recycler_view_chatList.visibility   = View.VISIBLE
                    tv_no_message.visibility            = View.GONE
                }else{
                    recycler_view_chatList.visibility   = View.GONE
                    tv_no_message.visibility            = View.VISIBLE
                }
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context , error.message , Toast.LENGTH_SHORT).show()
            }
        })
    }


    // function show latest message.
    @SuppressLint("SetTextI18n")
    fun retrieveLastMessage(context: Context , chatUserId : String , tv_last_message : TextView){
        lastMessage = "Default Message"

        chatsReference.addValueEventListener(object : ValueEventListener{
            override fun onDataChange(snapshot: DataSnapshot) {
                for (ds in snapshot.children){
                    val chat = ds.getValue(ChatModel::class.java)!!

                    if( chat.receiver == Constants.getCurrentUser() && chat.sender == chatUserId || chat.receiver == chatUserId &&  chat.sender == Constants.getCurrentUser()){
                        lastMessage = chat.message
                    }
                }
                when(lastMessage){
                    "Default Message" -> tv_last_message.text = "No Message"
                    "sent you an image." -> tv_last_message.text = "Image Sent."
                    else -> tv_last_message.text = lastMessage
                }
                lastMessage = "Default Message"
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context , error.message , Toast.LENGTH_SHORT).show()
            }
        })
    }
}