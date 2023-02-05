package com.example.firestorechattest

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.CollectionReference
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class SignUpActivity : AppCompatActivity() {

    private lateinit var signupEmail : EditText
    private lateinit var signupPassword : EditText
    private lateinit var signupName : EditText
    private lateinit var buttonSignup : Button
    private lateinit var mAuth : FirebaseAuth
    private lateinit var db : FirebaseFirestore
    private lateinit var usersRef : CollectionReference

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)


        mAuth = FirebaseAuth.getInstance()
        db = Firebase.firestore
        usersRef = db.collection("Users")
        signupEmail = findViewById(R.id.emailTextSignup)
        signupPassword = findViewById(R.id.passwordTextSignup)
        signupName = findViewById(R.id.nameTextSignup)
        buttonSignup = findViewById(R.id.signupBtnSignup)

        buttonSignup.setOnClickListener {
            val name = signupName.text.toString()
            val email = signupEmail.text.toString()
            val password = signupPassword.text.toString()

            signUp(name, email, password)
        }
    }

    private fun signUp(name : String, email : String, password : String) {

        if (email.isEmpty() || password.isEmpty()) {
            return
        }

        mAuth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    addUserToDatabase(name, email, mAuth.currentUser?.uid!!)
                    val intent = Intent(this@SignUpActivity, MainActivity::class.java)
                    finish()
                    startActivity(intent)
                } else {
                    Toast.makeText(this@SignUpActivity, "Authentication failed.", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun addUserToDatabase(name: String, email: String, uid: String) {

        usersRef.add(User(name, email, uid))
            .addOnSuccessListener { documentReference ->
                Toast.makeText(this@SignUpActivity, "DocumentSnapshot added with ID: ${documentReference.id}", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this@SignUpActivity, "Error handling document", Toast.LENGTH_SHORT).show()
            }

    }
}