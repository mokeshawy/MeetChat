package com.example.meetchat.adapter
import android.app.AlertDialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.RecyclerView
import com.example.meetchat.R
import com.example.meetchat.`interface`.OnClickUsersAdapter
import com.example.meetchat.databinding.UserSearchItemBinding
import com.example.meetchat.model.UsersModel
import com.example.meetchat.util.Constants
import com.example.meetchat.viewpagerfragment.ViewPagerFragment
import com.squareup.picasso.Picasso

class RecyclerUsersAdapter(private var mUsers: ArrayList<UsersModel> , var context: Context , var viewPagerFragment: ViewPagerFragment ) : RecyclerView.Adapter<RecyclerUsersAdapter.ViewHolder>() {

    class ViewHolder(var binding : UserSearchItemBinding) : RecyclerView.ViewHolder(binding.root) {

        // initialize onClickUsersAdapter from interface
//        fun initialize(viewHolder: ViewHolder, dataSet: UsersModel, action : OnClickUsersAdapter, isChecked : Boolean){
//            action.onClickUsersAdapter(viewHolder , dataSet , adapterPosition , isChecked)
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

        //viewHolder.initialize( viewHolder , mUsers[position] , onClickAdapter , false)

        viewHolder.itemView.setOnClickListener {

            // set click listener on item view
            viewHolder.itemView.setOnClickListener {
                val options = arrayOf<CharSequence>(
                    "Send Message",
                    "Visit Profile"
                )
                val builder = AlertDialog.Builder(context)
                builder.setTitle("What do you want?")
                builder.setItems(options){dialog,position->
                    if( position == 0){
                        var bundle = Bundle()
                        bundle.putSerializable(Constants.VISIT_ID , mUsers)
                        viewPagerFragment.findNavController().navigate(R.id.action_viewPagerFragment_to_messageChatFragment , bundle)
                    }
                    if( position == 1){

                    }
                }
                builder.setNegativeButton("cancel",null)
                builder.create().show()
            }
        }
    }


    // Return the size of your dataset (invoked by the layout manager)
    override fun getItemCount() = mUsers.size

}