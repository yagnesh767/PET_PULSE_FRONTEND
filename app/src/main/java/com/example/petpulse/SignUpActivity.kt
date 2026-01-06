package com.example.petpulse

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.petpulse.model.GeneralResponse
import com.example.petpulse.model.SignupRequest
import com.example.petpulse.network.ApiClient
import com.example.petpulse.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import org.json.JSONObject
import android.util.Log

class SignUpActivity : AppCompatActivity() {

    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPass: EditText
    private lateinit var signupEmail: String // To hold email for OTP screen

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)

        etName = findViewById(R.id.etFullName)
        etEmail = findViewById(R.id.etEmail)
        etPass = findViewById(R.id.etPassword)

        findViewById<Button>(R.id.btnSignUp).setOnClickListener {
            val fullName = etName.text.toString().trim()
            signupEmail = etEmail.text.toString().trim() // Save for OTP screen
            val password = etPass.text.toString().trim()

            if (fullName.isEmpty() || signupEmail.isEmpty() || password.isEmpty()) {
                showError("All fields required")
                return@setOnClickListener
            }

            val api = ApiClient.apiService
            val request = SignupRequest(full_name = fullName, email = signupEmail, password = password)

            api.signup(request).enqueue(object : Callback<GeneralResponse> {
                override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                    
                    // 1. Extract the message strictly
                    val body = response.body()
                    val msg = if (response.isSuccessful && body != null) {
                        body.message
                    } else {
                        val errorStr = response.errorBody()?.string()
                        if (!errorStr.isNullOrEmpty()) {
                            try {
                                JSONObject(errorStr).optString("message", errorStr)
                            } catch (e: Exception) {
                                errorStr
                            }
                        } else {
                            "Unknown Server Error"
                        }
                    }

                    // 2. Strict Logic Flow
                    when (msg) {
                        "OTP sent to email. Please verify to continue.",
                        "Account exists but not verified" -> {
                            Toast.makeText(this@SignUpActivity, msg, Toast.LENGTH_LONG).show()
                            openSignupOtp(signupEmail)
                        }

                        "Email already registered" -> {
                            Toast.makeText(this@SignUpActivity, msg, Toast.LENGTH_SHORT).show()
                            startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                        }

                        else -> {
                            showError(msg)
                        }
                    }
                }

                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    showError("Network error: ${t.message}")
                }
            })
        }

        findViewById<TextView>(R.id.tvSignIn).setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
        }
    }

    private fun openSignupOtp(email: String) {
        val intent = Intent(this, VerifySignupOtpActivity::class.java)
        intent.putExtra("email", email)
        startActivity(intent)
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
