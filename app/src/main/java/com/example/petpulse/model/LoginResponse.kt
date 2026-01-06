package com.example.petpulse.model

data class LoginResponse(
    val status: String,
    val message: String,
    val data: UserData?
)
