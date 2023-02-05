package com.example.firestorechattest

import com.google.firebase.firestore.DocumentId

class User(
            var name : String? = null,
            var email : String? = null,
            @DocumentId var uid : String? = null)