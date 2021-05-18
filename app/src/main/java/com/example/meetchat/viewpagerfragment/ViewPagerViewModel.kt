package com.example.meetchat.viewpagerfragment

import android.content.Context
import android.view.View
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.ViewModel
import androidx.navigation.Navigation
import androidx.viewpager2.widget.ViewPager2
import com.example.meetchat.R
import com.example.meetchat.adapter.ViewPagerAdapter
import com.example.meetchat.chatsfragment.ChatsFragment
import com.example.meetchat.model.ChatModel
import com.example.meetchat.searchfragment.SearchFragment
import com.example.meetchat.settingsfragment.SettingsFragment
import com.example.meetchat.util.Constants
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class ViewPagerViewModel  : ViewModel(){

    // Firebase instance
    var firebaseDatabase        = FirebaseDatabase.getInstance()
    var chatsReference          = firebaseDatabase.getReference(Constants.CHATS_REFERENCE)

    fun viewPagerWork( context: Context , viewPager2: ViewPager2 , tabLayout : TabLayout , fragmentManager: FragmentManager , lifecycle: Lifecycle ){

        // operation work for viewPager2 with fragment
        val fragmentsList = arrayListOf<Fragment>( ChatsFragment() , SearchFragment() , SettingsFragment())
        val adapter = ViewPagerAdapter( fragmentsList , fragmentManager , lifecycle)
        viewPager2.adapter = adapter

        var countUnReadMessage = 0
        // operation work for tabLayOut with viewPager2
        TabLayoutMediator( tabLayout , viewPager2){tab,position ->
            when(position){
                0 ->{
                    chatsReference.addValueEventListener(object : ValueEventListener{
                        override fun onDataChange(snapshot: DataSnapshot) {
                            for ( ds in snapshot.children){

                                var chat = ds.getValue(ChatModel::class.java)!!

                                if(chat.receiver == Constants.getCurrentUser() && !chat.seen ){
                                    countUnReadMessage +=1
                                }
                            }
                            if(countUnReadMessage == 0){
                                tab.text = "Chats"
                            }else{
                                tab.text = "($countUnReadMessage)Chat"
                            }
                        }

                        override fun onCancelled(error: DatabaseError) {
                            TODO("Not yet implemented")
                        }
                    })
                }
                1 ->{
                    tab.text = "Search"
                }
                2 ->{
                    tab.text = "Setting"
                }
            }
        }.attach()

    }
}
