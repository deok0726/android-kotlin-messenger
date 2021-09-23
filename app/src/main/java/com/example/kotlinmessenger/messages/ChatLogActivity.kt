package com.example.kotlinmessenger.messages

import android.content.ClipData
import android.nfc.Tag
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinmessenger.NewMessageActivity
import com.example.kotlinmessenger.R
import com.example.kotlinmessenger.models.ChatMessage
import com.example.kotlinmessenger.models.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ktx.database
import com.google.firebase.ktx.Firebase
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.user_row_new_message.view.*
import kotlinx.android.synthetic.main.activity_chat_log.*
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatLogActivity : AppCompatActivity() {

    companion object {
        val TAG = "ChatLog"
    }

    val adapter = GroupieAdapter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

        recyclerview_chatlog.adapter = adapter

        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = user?.username

//        setupDummyData()
        listenForMessages()

        send_button_chatlog.setOnClickListener {
            Log.d(TAG, "Attempt to send message")
            performSendMessage()
        }
    }

    private fun listenForMessages() {
        val database = Firebase.database("https://kotlin-messenger-f0dda-default-rtdb.asia-southeast1.firebasedatabase.app")
        val ref = database.getReference("/messages")

        ref.addChildEventListener(object: ChildEventListener {

            override fun onChildAdded(snapshot: DataSnapshot, previousChildName: String?) {
                // Bring messages from firebase
                val chatMessage = snapshot.getValue(ChatMessage::class.java)

                if (chatMessage != null) {
                    Log.d(TAG, chatMessage!!.text)

                    // Check where the message comes from or go to
                    if (chatMessage.fromId == FirebaseAuth.getInstance().uid) {
                        adapter.add(ChatFromItem(chatMessage.text))
                    } else {
                        adapter.add(ChatToItem(chatMessage.text))
                    }
                }
            }

            override fun onCancelled(error: DatabaseError) {

            }

            override fun onChildChanged(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildMoved(snapshot: DataSnapshot, previousChildName: String?) {

            }

            override fun onChildRemoved(snapshot: DataSnapshot) {

            }
        })
    }

    private fun performSendMessage() {
        // how do we actually send a message to firebase
        val text = message_edittext_chatlog.text.toString()

        val fromId = FirebaseAuth.getInstance().uid
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        val toId = user!!.uid

        val database = Firebase.database("https://kotlin-messenger-f0dda-default-rtdb.asia-southeast1.firebasedatabase.app")
        // push() automatically generates node
        val ref = database.getReference("/messages").push()

        if (fromId == null) return

        val chatMessage = ChatMessage(ref.key!!, text, fromId, toId, System.currentTimeMillis() / 1000)
        ref.setValue(chatMessage)
            .addOnSuccessListener {
                Log.d(TAG, "Saved out chat message: ${ref.key}")
            }
    }

    private fun setupDummyData() {
        val adapter = GroupieAdapter()

        adapter.add(ChatFromItem("From message"))
        adapter.add(ChatToItem("To message"))
        adapter.add(ChatFromItem("From message"))
        adapter.add(ChatToItem("To message"))
        adapter.add(ChatFromItem("From message"))
        adapter.add(ChatToItem("To message"))

        recyclerview_chatlog.adapter = adapter
    }
}

class ChatFromItem(val text: String): Item<GroupieViewHolder>() {
    // Access particular UI component
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textview_from_row.text = text
    }
    // Call layout
    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}

class ChatToItem(val text: String): Item<GroupieViewHolder>() {
    // Access particular UI component
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textview_to_row.text = text
    }
    // Call layout
    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}