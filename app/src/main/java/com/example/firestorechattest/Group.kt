package com.example.firestorechattest

import com.google.firebase.firestore.DocumentId

data class Group(@DocumentId
                var groupId : String? = null,
                var members : ArrayList<User>? = null)  {

}