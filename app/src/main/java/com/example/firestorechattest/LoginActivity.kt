package com.example.firestorechattest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth

class LoginActivity : AppCompatActivity() {

    private lateinit var loginEmail : EditText
    private lateinit var loginPassword : EditText
    private lateinit var buttonLogin : Button
    private lateinit var buttonSignup : Button
    private lateinit var mAuth : FirebaseAuth

    // Lenovo github test

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        mAuth = FirebaseAuth.getInstance()
        loginEmail = findViewById(R.id.emailTextSignup)
        loginPassword = findViewById(R.id.passwordTextSignup)
        buttonLogin = findViewById(R.id.loginBtn)
        buttonSignup = findViewById(R.id.signupBtnSignup)

        buttonSignup.setOnClickListener {
            val intent = Intent(this, SignUpActivity::class.java)
            startActivity(intent)
        }

        buttonLogin.setOnClickListener {
            val email = loginEmail.text.toString()
            val password = loginPassword.text.toString()

            login(email, password)
        }
    }

    private fun login(email : String, password : String) {
        mAuth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    val intent = Intent(this, MainActivity::class.java)
                    finish()
                    startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                    Toast.makeText(this@LoginActivity, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}