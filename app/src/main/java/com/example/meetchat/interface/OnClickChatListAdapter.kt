package com.example.meetchat.`interface`

import com.example.meetchat.adapter.RecyclerChatListAdapter
import com.example.meetchat.adapter.RecyclerUsersAdapter
import com.example.meetchat.model.UsersModel

interface OnClickChatListAdapter {


    fun onClickChatListAdapter(viewHolder: RecyclerChatListAdapter.ViewHolder, dataSet: UsersModel, position: Int, isChecked: Boolean)
}