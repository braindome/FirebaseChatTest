package com.example.firestorechattest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.ktx.Firebase
import java.io.Serializable

class MainActivity : AppCompatActivity() {

    private lateinit var userRecyclerView : RecyclerView
    private lateinit var userList: ArrayList<User>
    private lateinit var adapter : UserAdapter
    private lateinit var auth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var usersRef : CollectionReference
    private lateinit var generalChatFab : FloatingActionButton


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()
        db = Firebase.firestore
        usersRef = db.collection("Users")

        userList = ArrayList()
        adapter = UserAdapter(this, userList)

        userRecyclerView = findViewById(R.id.userRecyclerView)

        userRecyclerView.layoutManager = LinearLayoutManager(this)
        userRecyclerView.adapter = adapter

        generalChatFab = findViewById(R.id.generalChatFab)

        generalChatFab.setOnClickListener {
            val intent = Intent(this, GeneralChatActivity::class.java)
            startActivity(intent)
        }


        usersRef.addSnapshotListener() { snapshot, e ->
            if (snapshot != null) {
                userList.clear()
                for (document in snapshot.documents) {
                    val item = document.toObject<User>()
                    Log.d("uidTest", "Logged in user id: ${auth.currentUser?.uid}; Item uid: ${item?.uid}")
                    if (auth.currentUser?.uid != item?.uid) {
                        if (item != null) {
                            userList.add(item)
                        }
                    }
                }
                adapter.notifyDataSetChanged()
            }

        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            // logout
            auth.signOut()
            val intent = Intent(this@MainActivity, LoginActivity::class.java)
            finish()
            startActivity(intent)
            return true
        }

        return true
    }

}