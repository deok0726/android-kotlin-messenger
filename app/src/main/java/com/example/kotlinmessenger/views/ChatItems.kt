package com.example.kotlinmessenger.views

import com.example.kotlinmessenger.R
import com.example.kotlinmessenger.models.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.GroupieViewHolder
import com.xwray.groupie.Item
import kotlinx.android.synthetic.main.chat_from_row.view.*
import kotlinx.android.synthetic.main.chat_to_row.view.*

class ChatFromItem(val text: String, val user: User): Item<GroupieViewHolder>() {
    // Access particular UI component
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textview_from_row.text = text

        // Load our user image into the start
        val uri = user.profileImageUrl
        val targetImageView = viewHolder.itemView.imageview_from_row
        Picasso.get().load(uri).into(targetImageView)
    }
    // Call layout
    override fun getLayout(): Int {
        return R.layout.chat_from_row
    }
}

class ChatToItem(val text: String, val user: User): Item<GroupieViewHolder>() {
    // Access particular UI component
    override fun bind(viewHolder: GroupieViewHolder, position: Int) {
        viewHolder.itemView.textview_to_row.text = text

        // Load our user image into the start
        val uri = user.profileImageUrl
        val targetImageView = viewHolder.itemView.imageview_to_row
        Picasso.get().load(uri).into(targetImageView)
    }
    // Call layout
    override fun getLayout(): Int {
        return R.layout.chat_to_row
    }
}