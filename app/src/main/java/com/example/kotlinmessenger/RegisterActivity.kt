package com.example.kotlinmessenger

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.ActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.google.firebase.storage.ktx.storage
import org.w3c.dom.Text
import java.util.*

class RegisterActivity : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private val registerButton: Button by lazy { findViewById(R.id.register_button_register) }
    private val accountAlready: TextView by lazy { findViewById(R.id.already_have_account_textview) }
    private val selectPhotoButton: Button by lazy { findViewById(R.id.selectphoto_button_register) }
    private val username: EditText by lazy { findViewById(R.id.username_edittext_register) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_register)

        // Initialize Firebase Auth
        auth = Firebase.auth

        registerButton.setOnClickListener {
            performRegister()
        }

        accountAlready.setOnClickListener {
            Log.d("RegisterActivity", "Try to show login activity")

            // launch the login activity somehow
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }

        selectPhotoButton.setOnClickListener {
            Log.d("RegisterActivity", "Try to show photo selector")

            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"

            openActivityForResult(intent)
        }

    }

    private var selectedPhotoUri: Uri? = null

    private val resultLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            Log.d("RegisterActivity", "Photo was selected")

            val data: Intent? = result.data
            selectedPhotoUri = data?.data

            val bitmap = MediaStore.Images.Media.getBitmap(contentResolver, selectedPhotoUri)
            val bitmapDrawable = BitmapDrawable(bitmap)
            selectPhotoButton.setBackgroundDrawable(bitmapDrawable)

        }
    }

    private fun openActivityForResult(intent: Intent) {
        resultLauncher.launch(intent)
    }

    private fun performRegister() {
        val email: EditText = findViewById(R.id.email_edittext_register)
        val password: EditText = findViewById(R.id.password_edittext_register)
        val emailText = email.text.toString()
        val passwordText = password.text.toString()

        if (emailText.isEmpty() || passwordText.isEmpty()) {
            Log.d("RegisterActivity", "Please enter text in email/pw")
            Toast.makeText(this, "Please enter text in email/pw", Toast.LENGTH_SHORT).show()
            return
        }

        Log.d("RegisterActivity", "Email is $emailText")
        Log.d("RegisterActivity", "password is $passwordText")

        // Firebase Authentication to create a user with email and password
        auth.createUserWithEmailAndPassword(emailText, passwordText)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d("RegisterAuth", "createUserWithEmail:success")
                    uploadImageToFirebaseStorage()
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w("RegisterAuth", "createUserWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun uploadImageToFirebaseStorage() {
        if (selectedPhotoUri == null) return

        val storage = Firebase.storage
        val filename = UUID.randomUUID().toString()
        val ref = storage.getReference("/images/$filename")

        ref.putFile(selectedPhotoUri!!)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Successfully uploaded image: ${it.metadata?.path}")

                ref.downloadUrl.addOnSuccessListener {
                    Log.d("RegisterActivity", "Image file Location: $it")

                    saveUserToFirebaseDatabase(it.toString())
                }
            }
            .addOnFailureListener {
                Log.d("RegisterActivity", "Fail to store image file")
            }
    }

    private fun saveUserToFirebaseDatabase(profileImageUrl: String) {
        val database = Firebase.database("https://kotlin-messenger-f0dda-default-rtdb.asia-southeast1.firebasedatabase.app")
        val uid = auth.uid?: ""
        val ref = database.getReference("/users/$uid")
        val user = User(uid, username.text.toString(), profileImageUrl)

        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("RegisterActivity", "Finally saved the user to Firebase database")
            }
            .addOnFailureListener {
                Log.d("RegisterActivity", "Fail to save the user to Firebase database")
            }
    }

}

class User(val uid: String, val username: String, val profileImageUrl: String)