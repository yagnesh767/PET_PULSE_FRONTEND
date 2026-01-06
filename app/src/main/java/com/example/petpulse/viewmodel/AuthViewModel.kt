package com.example.petpulse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import com.example.petpulse.model.ForgotPasswordRequest
import com.example.petpulse.model.GeneralResponse
import com.example.petpulse.model.LoginRequest
import com.example.petpulse.model.LoginResponse
import com.example.petpulse.model.ResetPasswordRequest
import com.example.petpulse.model.VerifyOtpRequest
import com.example.petpulse.model.SignupRequest
import com.example.petpulse.network.ApiClient
import com.example.petpulse.network.ApiService
import com.google.gson.Gson
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AuthViewModel : ViewModel() {

    private val _loginResult = MutableLiveData<Result<LoginResponse>>()
    val loginResult: LiveData<Result<LoginResponse>> = _loginResult

    private val _signupResult = MutableLiveData<Result<GeneralResponse>>()
    val signupResult: LiveData<Result<GeneralResponse>> = _signupResult

    private val _otpResult = MutableLiveData<Result<GeneralResponse>>()
    val otpResult: LiveData<Result<GeneralResponse>> = _otpResult

    private val _forgotResult = MutableLiveData<Result<GeneralResponse>>()
    val forgotResult: LiveData<Result<GeneralResponse>> = _forgotResult

    private val _resetPasswordResult = MutableLiveData<Result<GeneralResponse>>()
    val resetPasswordResult: LiveData<Result<GeneralResponse>> = _resetPasswordResult

    private fun getErrorMessage(errorBody: String?): String {
        if (errorBody == null) return "Unknown error"
        return try {
            Gson().fromJson(errorBody, GeneralResponse::class.java).message
        } catch (e: Exception) {
            "An error occurred"
        }
    }

    fun login(email: String, password: String) {
        viewModelScope.launch {
            try {
                val response = ApiClient.apiService.login(LoginRequest(email, password))

                if (response.isSuccessful && response.body() != null) {
                    _loginResult.postValue(Result.success(response.body()!!))
                } else {
                    _loginResult.postValue(Result.failure(Exception("Invalid response")))
                }
            } catch (e: Exception) {
                _loginResult.postValue(Result.failure(e))
            }
        }
    }

    fun signup(fullName: String, email: String, password: String) {
        val api = ApiClient.apiService
        val request = SignupRequest(
            full_name = fullName,
            email = email,
            password = password
        )

        api.signup(request).enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.status == "success") {
                        _signupResult.postValue(Result.success(body))
                    } else {
                        val msg = body?.message ?: "Signup failed"
                        _signupResult.postValue(Result.failure(Exception(msg)))
                    }
                } else {
                    val errorMessage = getErrorMessage(response.errorBody()?.string())
                    _signupResult.postValue(Result.failure(Exception(errorMessage)))
                }
            }

            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                _signupResult.postValue(Result.failure(t))
            }
        })
    }

    fun verifyOtp(email: String, otp: String) {
        val api = ApiClient.apiService
        val otpMap = mapOf("email" to email, "otp" to otp)

        api.verifyOtp(otpMap).enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                 if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.status == "success") {
                        _otpResult.postValue(Result.success(body))
                    } else {
                        val errorMessage = body?.message ?: "OTP verification failed"
                        _otpResult.postValue(Result.failure(Exception(errorMessage)))
                    }
                } else {
                    val errorMessage = getErrorMessage(response.errorBody()?.string())
                    _otpResult.postValue(Result.failure(Exception(errorMessage)))
                }
            }

            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                _otpResult.postValue(Result.failure(t))
            }
        })
    }

    fun forgotPassword(email: String) {
        val api = ApiClient.apiService
        val request = ForgotPasswordRequest(email)

        api.forgotPassword(request).enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.status == "success") {
                        _forgotResult.postValue(Result.success(body))
                    } else {
                        val errorMessage = body?.message ?: "Request failed"
                        _forgotResult.postValue(Result.failure(Exception(errorMessage)))
                    }
                } else {
                    val errorMessage = getErrorMessage(response.errorBody()?.string())
                    _forgotResult.postValue(Result.failure(Exception(errorMessage)))
                }
            }

            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                _forgotResult.postValue(Result.failure(t))
            }
        })
    }

    fun verifyResetOtp(email: String, otp: String) {
        val api = ApiClient.apiService
        val request = VerifyOtpRequest(email, otp)

        api.verifyResetOtp(request).enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                 if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.status == "success") {
                        _otpResult.postValue(Result.success(body))
                    } else {
                        val errorMessage = body?.message ?: "OTP verification failed"
                        _otpResult.postValue(Result.failure(Exception(errorMessage)))
                    }
                } else {
                    val errorMessage = getErrorMessage(response.errorBody()?.string())
                    _otpResult.postValue(Result.failure(Exception(errorMessage)))
                }
            }

            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                _otpResult.postValue(Result.failure(t))
            }
        })
    }

    fun resetPassword(email: String, otp: String, new_password: String) {
        val api = ApiClient.apiService
        val request = ResetPasswordRequest(email, otp, new_password)

        api.resetPassword(request).enqueue(object : Callback<GeneralResponse> {
            override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                 if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.status == "success") {
                        _resetPasswordResult.postValue(Result.success(body))
                    } else {
                        val errorMessage = body?.message ?: "Password reset failed"
                        _resetPasswordResult.postValue(Result.failure(Exception(errorMessage)))
                    }
                } else {
                    val errorMessage = getErrorMessage(response.errorBody()?.string())
                    _resetPasswordResult.postValue(Result.failure(Exception(errorMessage)))
                }
            }

            override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                _resetPasswordResult.postValue(Result.failure(t))
            }
        })
    }
}
