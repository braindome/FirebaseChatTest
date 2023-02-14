package com.example.firestorechattest

import com.google.firebase.firestore.ServerTimestamp
import java.util.*

class Message(
    var message : String? = null,
    var senderId : String? = null,
    @ServerTimestamp val timestamp : Date? = null) {
}