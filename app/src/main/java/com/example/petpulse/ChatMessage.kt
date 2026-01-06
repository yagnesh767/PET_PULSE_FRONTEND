package com.example.petpulse

data class ChatMessage(
    val message: String,
    val isUserMessage: Boolean,
    val isIntro: Boolean = false
)
