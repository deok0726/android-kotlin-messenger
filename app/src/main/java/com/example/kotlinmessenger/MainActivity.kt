package com.example.kotlinmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val registerButton: Button = findViewById(R.id.register_button_register)
        val accountAlready: TextView = findViewById(R.id.already_have_account_text_view)

        registerButton.setOnClickListener {
            val email: EditText = findViewById(R.id.email_edittext_register)
            val password: EditText = findViewById(R.id.password_edittext_register)
            Log.d("MainActivity", "Email is " + email.text.toString())
            Log.d("MainActivity", "password is " + password.text.toString())

            // Firebase Authentification to create a user with email and password
        }

        accountAlready.setOnClickListener {
            Log.d("MainActivity", "Try to show login activity")

            // launch the login activity somehow
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


    }
}