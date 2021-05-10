package com.example.meetchat.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.meetchat.databinding.UserSearchItemBinding
import com.example.meetchat.model.UsersModel
import com.squareup.picasso.Picasso

class RecyclerUsersAdapter(private var mUsers: ArrayList<UsersModel> ,
                           var mContext : Context ,
                           var isChecked : Boolean) : RecyclerView.Adapter<RecyclerUsersAdapter.ViewHolder>() {

    class ViewHolder(var binding : UserSearchItemBinding) : RecyclerView.ViewHolder(binding.root) {

//        fun initialize(viewHolder: ViewHolder , dataSet: UsersModel , action : OnClickAdapter){
//            action.onClickUsersAdapter(viewHolder , dataSet , adapterPosition)
//        }

    }
    // Create new views (invoked by the layout manager)
    override fun onCreateViewHolder(viewGroup: ViewGroup, viewType: Int): ViewHolder {

        // Create a new view, which defines the UI of the list item
        return ViewHolder(UserSearchItemBinding.inflate(LayoutInflater.from(viewGroup.context),viewGroup,false))
    }

    // Replace the contents of a view (invoked by the layout manager)
    override fun onBindViewHolder(viewHolder: ViewHolder, position: Int) {

        // Get element from your dataset at this position and replace the
        // contents of the view with that element

        viewHolder.binding.tvUserName.text = mUsers[position].username
        Picasso.get().load(mUsers[position].profile).into(viewHolder.binding.ivProfileImage)


//        viewHolder.initialize( viewHolder , dataSet[position] , onClickAdapter)
    }


    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = mUsers.size

}