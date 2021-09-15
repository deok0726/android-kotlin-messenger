package com.example.kotlinmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class MainActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val registerButton: Button = findViewById(R.id.register_button_register)
        val accountAlready: TextView = findViewById(R.id.already_have_account_textview)
        // Initialize Firebase Auth
        auth = Firebase.auth

        registerButton.setOnClickListener {
            performRegister()
        }

        accountAlready.setOnClickListener {
            Log.d("MainActivity", "Try to show login activity")

            // launch the login activity somehow
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


    }
    private fun performRegister() {
        val email: EditText = findViewById(R.id.email_edittext_register)
        val password: EditText = findViewById(R.id.password_edittext_register)
        val emailText = email.text.toString()
        val passwordText = password.text.toString()

        if (emailText.isEmpty() || passwordText.isEmpty()) {
            Toast.makeText(this, "Please enter text in email/pw", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("MainActivity", "Email is $emailText")
        Log.d("MainActivity", "password is $passwordText")

        // Firebase Authentication to create a user with email and password
        auth.createUserWithEmailAndPassword(emailText, passwordText)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("Auth", "createUserWithEmail:success")
                    val user = auth.currentUser
//                        updateUI(user)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("Auth", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
//                        updateUI(null)
                }
            }
    }
}