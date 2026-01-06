package com.example.petpulse.model

data class VerifyOtpRequest(
    val email: String,
    val otp: String
)
