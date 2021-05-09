package com.example.meetchat.model

import java.io.Serializable

data class UsersModel( var uid          : String = "",
                       var username     : String = "",
                       var profile      : String = "",
                       var cover        : String = "",
                       var status       : String = "",
                       var search       : String = "",
                       var facebook     : String = "",
                       var instagram    : String = "",
                       var website      : String = "" ) : Serializable