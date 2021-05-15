package com.example.meetchat.searchfragment


import android.content.Context
import android.widget.EditText
import android.widget.Toast
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.meetchat.model.UsersModel
import com.example.meetchat.util.Constants
import com.google.firebase.database.*

class SearchViewModel : ViewModel() {

    var etSearchUser = MutableLiveData<String>("")
    var mUsersAdapterLiveData   = MutableLiveData<ArrayList<UsersModel>>()
    private var mUsers          = ArrayList<UsersModel>()

    var userReference   = FirebaseDatabase.getInstance().getReference(Constants.USER_REFERENCE)
    var queryUsers      = FirebaseDatabase.getInstance().getReference(Constants.USER_REFERENCE)

    // function retrieve all users
     fun retrieveAllUsers( context: Context ){
        userReference.addValueEventListener( object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                mUsers!!.clear()
                if(etSearchUser.value!! == ""){
                    for (ds in snapshot.children){

                        var users = ds.getValue(UsersModel::class.java)!!
                        if(users.uid != Constants.getCurrentUser()){
                            mUsers!!.add(users)
                        }
                    }
                }
                mUsersAdapterLiveData.value = mUsers
            }
            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(context , error.message , Toast.LENGTH_SHORT).show()
            }
        })
    }

    // function search for user
    fun searchForUser( context: Context , string : String  ){
        queryUsers.orderByChild(Constants.USER_SEARCH)
            .startAt(string)
            .endAt(string + "\uf8ff")
            .addValueEventListener( object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    mUsers!!.clear()
                    for (ds in snapshot.children){

                        var users = ds.getValue(UsersModel::class.java)!!
                        if(users.uid != Constants.getCurrentUser()){
                            mUsers!!.add(users)
                        }
                    }
                    mUsersAdapterLiveData.value = mUsers
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(context , error.message , Toast.LENGTH_SHORT).show()
                }
            })
    }

}