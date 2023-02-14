package com.example.firestorechattest

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase

class GeneralChatActivity : AppCompatActivity() {

    private lateinit var chatRecyclerView: RecyclerView
    private lateinit var messageBox : EditText
    private lateinit var sendButton : ImageView
    private lateinit var messageAdapter: MessageAdapter
    private lateinit var messageList: ArrayList<Message>
    private lateinit var uidRef : CollectionReference
    private lateinit var db : FirebaseFirestore
    private lateinit var messagesRef : CollectionReference

    var receiverRoom : String? = null
    var senderRoom : String? = null
    val generalRoom : String = "dummychat"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat)

        val name = intent.getStringExtra("name")
        val receiverUid = intent.getStringExtra("uid")

        val senderUid = FirebaseAuth.getInstance().currentUser?.uid

        db = Firebase.firestore

        messagesRef = db.collection("Chats")
        uidRef = db.collection("Users")

        supportActionBar?.title = name

        chatRecyclerView = findViewById(R.id.chatRecycleView)
        messageBox = findViewById(R.id.messageBox)
        sendButton = findViewById(R.id.sendButton)
        messageList = ArrayList()
        messageAdapter = MessageAdapter(this, messageList)
        chatRecyclerView.layoutManager = LinearLayoutManager(this)
        chatRecyclerView.adapter = messageAdapter

        messagesRef.document(generalRoom).collection("Messages")
            .orderBy("timestamp")
            .addSnapshotListener { snapshot, e ->
                messageList.clear()
                if (snapshot != null) {
                    for (document in snapshot.documents) {
                        val item = document.toObject<Message>()
                        messageList.add(item!!)
                    }
                    messageAdapter.notifyDataSetChanged()
                }
            }


        // adding the message to database
        sendButton.setOnClickListener {

            val message = messageBox.text.toString()
            val senderUid = FirebaseAuth.getInstance().currentUser?.uid
            val messageObject = Message(message, senderUid)

            messagesRef.document(generalRoom).collection("Messages")
                .add(messageObject)
                .addOnSuccessListener { documentReference ->
                    messagesRef.document("$receiverRoom").collection("Messages")
                        .add(messageObject)
                    Log.d("msg", "DocumentSnapshot written with ID: ${documentReference.id}")
                }
                .addOnFailureListener { e ->
                    Log.d("msg", "Error adding document", e)
                }



            messageBox.setText("")


        }
    }
}
