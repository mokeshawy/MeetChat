package com.example.meetchat.activitis

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import com.example.meetchat.R
import com.example.meetchat.databinding.ActivityMainBinding
import com.example.meetchat.util.Constants
import com.google.firebase.database.FirebaseDatabase

class MainActivity : AppCompatActivity() {

    lateinit var binding : ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this , R.layout.activity_main)

        // operation work for navigation component for fragment
        val navHostFragment : NavHostFragment   = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        val navController   : NavController     = navHostFragment.navController

        // set up action bar for fragment
        val appBarConfiguration = AppBarConfiguration(setOf())
        setupActionBarWithNavController(navController , appBarConfiguration)


        //hide action bar for fragment
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id){

                R.id.splashFragment         -> supportActionBar!!.hide()
                R.id.viewPagerFragment      -> supportActionBar!!.hide()
                R.id.registerFragment       -> supportActionBar!!.hide()
                R.id.loginFragment          -> supportActionBar!!.hide()
                R.id.forgetPasswordFragment -> supportActionBar!!.hide()
                R.id.searchFragment         -> supportActionBar!!.hide()
                R.id.chatsFragment          -> supportActionBar!!.hide()
                R.id.messageChatFragment    -> supportActionBar!!.hide()
                R.id.viewFullImageFragment  -> supportActionBar!!.hide()

                else -> supportActionBar!!.show()
            }
        }
    }


    private fun updateStatus(status : String){
        var firebaseDatabase        = FirebaseDatabase.getInstance()
        var usersReference          = firebaseDatabase.getReference(Constants.USER_REFERENCE)
        var map = HashMap<String , Any>()

        map[Constants.USER_STATUS] = status
        usersReference.child(Constants.getCurrentUser()).updateChildren(map)
    }

    override fun onResume() {
        super.onResume()
        updateStatus("Online")
    }

    override fun onPause() {
        super.onPause()
        updateStatus("Offline")
    }
}