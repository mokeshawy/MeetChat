package com.example.meetchat.model

data class ChatModel (

     var sender     : String = "",
     var message    : String = "",
     var receiver   : String = "",
     var isSeen     : Boolean = false,
     var url        : String = "",
     var messageId  : String = ""

)