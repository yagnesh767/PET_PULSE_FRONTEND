package com.example.petpulse.model

data class GeneralResponse(
    val status: String,
    val message: String,
    val debug_otp: String? = null
)
