package com.example.firestorechattest

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

open class Message(
    var message : String? = null,
    var senderId : String? = null,
    var senderName : String? = null,
    @ServerTimestamp val timestamp : Date? = null) {
}