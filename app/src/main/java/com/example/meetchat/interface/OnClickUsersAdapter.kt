package com.example.meetchat.`interface`

import com.example.meetchat.adapter.RecyclerChatAdapter
import com.example.meetchat.adapter.RecyclerUsersAdapter
import com.example.meetchat.model.ChatModel
import com.example.meetchat.model.UsersModel

interface OnClickUsersAdapter {

    // Fun on click item from users adapter
    fun onClickUsersAdapter( viewHolder: RecyclerUsersAdapter.ViewHolder , dataSet : UsersModel , position : Int , isChecked : Boolean)

}