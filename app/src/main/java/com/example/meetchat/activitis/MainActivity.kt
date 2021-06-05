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
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    lateinit var binding            : ActivityMainBinding
    lateinit var navHostFragment    : NavHostFragment
    lateinit var navController      : NavController
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this , R.layout.activity_main)

        // operation work for navigation component for fragment
        navHostFragment     = supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment
        navController       = navHostFragment.navController

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
                R.id.visitUserProfileFragment -> supportActionBar!!.hide()

                else -> supportActionBar!!.show()
            }
        }
    }


    override fun onPause() {
        super.onPause()

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.viewPagerFragment -> {
                    CoroutineScope(Dispatchers.IO).async {
                        Constants.updateStatus("Offline")
                    }
                }

                R.id.messageChatFragment -> {
                    CoroutineScope(Dispatchers.IO).async {
                        Constants.updateStatus("Offline")
                    }
                }

                R.id.viewFullImageFragment -> {
                    CoroutineScope(Dispatchers.IO).async {
                        Constants.updateStatus("Offline")
                    }
                }

                R.id.visitUserProfileFragment -> {
                    CoroutineScope(Dispatchers.IO).async {
                        Constants.updateStatus("Offline")
                    }
                }

            }
        }
    }

    override fun onResume() {
        super.onResume()

        navController.addOnDestinationChangedListener { _, destination, _ ->
            when(destination.id){
                R.id.viewPagerFragment ->{
                    CoroutineScope(Dispatchers.Main).async {
                        Constants.updateStatus("Online")
                    }
                }

                R.id.messageChatFragment -> {
                    CoroutineScope(Dispatchers.Main).async {
                        Constants.updateStatus("Online")
                    }
                }

                R.id.viewFullImageFragment -> {
                    CoroutineScope(Dispatchers.Main).async {
                        Constants.updateStatus("Online")
                    }
                }

                R.id.visitUserProfileFragment -> {
                    CoroutineScope(Dispatchers.Main).async {
                        Constants.updateStatus("Online")
                    }
                }
            }
        }
    }
}