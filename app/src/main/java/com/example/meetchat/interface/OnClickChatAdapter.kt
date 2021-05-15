package com.example.meetchat.`interface`

import com.example.meetchat.adapter.RecyclerChatAdapter
import com.example.meetchat.model.ChatModel

interface OnClickChatAdapter {

    fun onClickMessageItem(viewHolder: RecyclerChatAdapter.ViewHolder, mChatList : ArrayList<ChatModel> , dataSet : ChatModel, position : Int)
}