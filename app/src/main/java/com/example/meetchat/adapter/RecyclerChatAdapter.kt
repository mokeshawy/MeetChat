package com.example.meetchat.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import com.example.meetchat.R
import com.example.meetchat.`interface`.OnClickChatAdapter
import com.example.meetchat.model.ChatModel
import com.example.meetchat.util.Constants
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

class RecyclerChatAdapter (private var mChatList        : ArrayList<ChatModel> ,
                                   var imageUrl         : String ,
                                   var onClickMessage   : OnClickChatAdapter
                           ) : RecyclerView.Adapter<RecyclerChatAdapter.ViewHolder>() {

    private val MESSAGE_TYPE_LEFT   = 0
    private val MESSAGE_TYPE_RIGHT  = 1

    class ViewHolder(view : View) : RecyclerView.ViewHolder(view) {


        lateinit var profileImage       : CircleImageView
        lateinit var showTextMessage    : TextView
        lateinit var textSeen           : TextView
                 var rightImageView     : ImageView? = null
                 var leftImageView      : ImageView? = null

        init {
                profileImage    = view.findViewById(R.id.iv_profile_image)
                showTextMessage = view.findViewById(R.id.tv_show_text_message)
                leftImageView   = view.findViewById(R.id.iv_left_image_view)
                textSeen        = view.findViewById(R.id.tv_text_seen)
                rightImageView  = view.findViewById(R.id.iv_right_image_view)
        }

        // fun initialize for working interface operation
        fun initialize( viewHolder: ViewHolder , mChatList: ArrayList<ChatModel> , mChat: ChatModel , action : OnClickChatAdapter){
            action.onClickMessageItem( viewHolder , mChatList ,mChat , adapterPosition )


        }
    }
    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {
        // multi-layout
        return if(viewType == MESSAGE_TYPE_RIGHT){
            var view = LayoutInflater.from(viewGroup.context).inflate(R.layout.message_item_right , viewGroup , false)
             ViewHolder(view)
        }else{
            var view = LayoutInflater.from(viewGroup.context).inflate(R.layout.message_item_left , viewGroup , false)
            ViewHolder(view)
        }
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {
        var mChat = mChatList[position]

        // show profile image for user receiver.
        Picasso.get().load(imageUrl).into(viewHolder.profileImage)

        // call fun initialize for on ClickItem
        viewHolder.initialize(viewHolder , mChatList ,mChat , onClickMessage)
    }


    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = mChatList.size

    // fun get view type.
    @Suppress("UNREACHABLE_CODE")
    override fun getItemViewType(position: Int): Int {
        if(mChatList[position].sender == Constants.getCurrentUser()){
            return MESSAGE_TYPE_RIGHT
        }else{
            return MESSAGE_TYPE_LEFT
        }
    }
}