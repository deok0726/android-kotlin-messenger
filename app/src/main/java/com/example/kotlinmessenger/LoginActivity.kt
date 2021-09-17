package com.example.kotlinmessenger

import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

class LoginActivity: AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val loginButton: Button = findViewById(R.id.login_button_login)
        val backRegister: TextView = findViewById(R.id.back_to_register_textview)

        // Initialize Firebase Auth
        auth = Firebase.auth

        loginButton.setOnClickListener {
            performLogin()
        }
        backRegister.setOnClickListener {
            finish()
        }

    }

    private fun performLogin() {
        val email: EditText = findViewById(R.id.email_edittext_login)
        val password: EditText = findViewById(R.id.password_edittext_login)
        val emailText = email.text.toString()
        val passwordText = password.text.toString()

        Log.d("Login", "Attempt login with email/pw: $emailText/$passwordText")
        auth.signInWithEmailAndPassword(emailText, passwordText)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("LoginAuth", "signInWithEmail:success")
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("LoginAuth", "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }
}