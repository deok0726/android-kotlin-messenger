package com.example.kotlinmessenger.messages

import android.content.ClipData
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.RecyclerView
import com.example.kotlinmessenger.NewMessageActivity
import com.example.kotlinmessenger.R
import com.example.kotlinmessenger.models.User
import com.squareup.picasso.Picasso
import kotlinx.android.synthetic.main.activity_chat_log.*
import com.xwray.groupie.GroupieAdapter
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.user_row_new_message.view.*

class ChatLogActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_log)

//        val userName = intent.getStringExtra(NewMessageActivity.USER_KEY)
        val user = intent.getParcelableExtra<User>(NewMessageActivity.USER_KEY)
        supportActionBar?.title = user?.username

        val adapter = GroupieAdapter()

        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())
        adapter.add(ChatFromItem())
        adapter.add(ChatToItem())

        recyclerview_chatlog.adapter = adapter
    }
}

class ChatFromItem(): Item<GroupieViewHolder>() {
    // Access particular UI component
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }
    // Call layout
    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}

class ChatToItem(): Item<GroupieViewHolder>() {
    // Access particular UI component
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {

    }
    // Call layout
    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}