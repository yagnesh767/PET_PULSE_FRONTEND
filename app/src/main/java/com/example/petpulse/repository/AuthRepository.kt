package com.example.petpulse.repository

import com.example.petpulse.network.ApiClient
import com.example.petpulse.network.ApiService
import com.example.petpulse.model.LoginRequest
import com.example.petpulse.model.SignupRequest
import com.example.petpulse.model.VerifyOtpRequest

class AuthRepository {
    private val apiService = ApiClient.apiService

    suspend fun login(email: String, password: String) = apiService.login(LoginRequest(email, password))

    suspend fun signup(fullName: String, email: String, password: String) = 
        apiService.signup(SignupRequest(fullName, email, password))

    suspend fun verifyOtp(email: String, otp: String) = 
        apiService.verifyOtp(mapOf("email" to email, "otp" to otp))
}
