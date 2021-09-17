package com.example.kotlinmessenger

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinmessenger.messages.ChatLogActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.ValueEventListener
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import org.w3c.dom.Text
import kotlinx.android.synthetic.main.user_row_new_message.*
import kotlinx.android.synthetic.main.activity_new_message.*
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class NewMessageActivity : AppCompatActivity() {
    companion object {
        val TAG = "NewMessageActivity"
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new_message)

        // Change title from project name to new one
        supportActionBar?.title = "Select User"

        // Fetch user info from firebase database to upload on NewMessageActivity
        fetchUsers()
    }

    private fun fetchUsers() {
        val database = Firebase.database("https://kotlin-messenger-f0dda-default-rtdb.asia-southeast1.firebasedatabase.app")
        val ref = database.getReference("/users")

        // Use single value event listener to prevent event listener keep updating whenever data changes
        ref.addListenerForSingleValueEvent(object: ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val adapter = GroupieAdapter()

                snapshot.children.forEach() {
                    Log.d(TAG, it.toString())

                    // Call User class from RegisterActivity
                    val user = it.getValue(User::class.java)
                    if (user != null) {
                        // Add the number of users in database
                        adapter.add(UserItem(user))
                    }
                }

                adapter.setOnItemClickListener { item, view ->

                    val intent = Intent(view.context,  ChatLogActivity::class.java)
                    startActivity(intent)

                    // Go back to LatestMessageActivity
                    finish()
                }
                recyclerview_newmessage.adapter = adapter
            }
            override fun onCancelled(error: DatabaseError) {

            }
        })
    }
}

class UserItem(val user: User): Item<GroupieViewHolder>() {
    // Access particular UI component
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.username_textview_newmessage.text = user.username
        // Put image to imageview
        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.imageview_newmessage)
    }

    override fun getLayout(): Int {
        return R.layout.user_row_new_message
    }
}
