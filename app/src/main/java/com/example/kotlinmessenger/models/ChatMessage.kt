package com.example.kotlinmessenger.models

// Store text parameter as database item
class ChatMessage(val id: String, val text: String, val fromId: String, val toId: String, val timeStamp: Long) {
    constructor() : this("", "", "", "", -1)
}