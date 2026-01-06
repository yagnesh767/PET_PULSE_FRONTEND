package com.example.petpulse

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.petpulse.model.LoginResponse
import com.example.petpulse.viewmodel.AuthViewModel

class LoginActivity : AppCompatActivity() {

    private val viewModel: AuthViewModel by viewModels()

    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        etEmail = findViewById(R.id.etEmail)
        etPassword = findViewById(R.id.etPassword)

        findViewById<TextView>(R.id.tvSignUp).setOnClickListener {
            startActivity(Intent(this, SignUpActivity::class.java))
        }

        findViewById<TextView>(R.id.tvForgotPassword).setOnClickListener {
            startActivity(Intent(this, ForgotPasswordActivity::class.java))
        }

        findViewById<Button>(R.id.btnSignIn).setOnClickListener {
            val email = etEmail.text.toString().trim()
            val password = etPassword.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Enter email and password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.login(email, password)
        }

        viewModel.loginResult.observe(this) { result ->

            result.onSuccess { response ->

                if (response.status == "success" && response.data != null) {

                    val user = response.data
                    saveUserSession(
                        user.user_id,
                        user.full_name,
                        user.is_premium
                    )

                    startActivity(Intent(this, MainActivity::class.java))
                    finish()

                } else {
                    showError(response.message)
                }
            }

            result.onFailure { error ->
                showError(error.message ?: "Login failed")
            }
        }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }

    private fun saveUserSession(
        userId: Int,
        fullName: String,
        isPremium: Int
    ) {
        val sharedPref =
            getSharedPreferences("user_session", Context.MODE_PRIVATE)

        with(sharedPref.edit()) {
            putInt("user_id", userId)
            putString("full_name", fullName)
            putBoolean("is_premium", isPremium == 1)
            apply()
        }
    }
}
