package com.example.petpulse

import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petpulse.network.ApiClient
import com.example.petpulse.model.GeneralResponse
import com.example.petpulse.model.VerifyOtpRequest
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VerifySignupOtpActivity : AppCompatActivity() {

    private lateinit var email: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_verify_otp)

        email = intent.getStringExtra("email") ?: ""

        val etOtp = findViewById<EditText>(R.id.etOtp)
        val btnVerify = findViewById<Button>(R.id.btnVerify)
        val btnResendOtp = findViewById<TextView>(R.id.btnResendOtp)

        btnVerify.setOnClickListener {
            val otp = etOtp.text.toString().trim()

            if (otp.length != 6) {
                toast("Enter valid 6-digit OTP")
                return@setOnClickListener
            }

            verifyOtp(email, otp)
        }

        btnResendOtp.setOnClickListener {
            resendOtp(email)
        }
    }

    private fun verifyOtp(email: String, otp: String) {
        val body = mapOf(
            "email" to email,
            "otp" to otp
        )

        ApiClient.apiService.verifyOtp(body)
            .enqueue(object : Callback<GeneralResponse> {
                override fun onResponse(
                    call: Call<GeneralResponse>,
                    response: Response<GeneralResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        toast(response.body()!!.message)
                        if (response.body()!!.status == "success") {
                            // Proceed to Login
                            finish()
                        }
                    } else {
                        toast("Verification failed")
                    }
                }

                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    toast("Network error")
                }
            })
    }

    private fun resendOtp(email: String) {
        val body = mapOf("email" to email)

        ApiClient.apiService.resendOtp(body)
            .enqueue(object : Callback<GeneralResponse> {
                override fun onResponse(
                    call: Call<GeneralResponse>,
                    response: Response<GeneralResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        toast(response.body()!!.message)
                    } else {
                        toast("Failed to resend OTP")
                    }
                }

                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    toast("Network error")
                }
            })
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
    }
}
