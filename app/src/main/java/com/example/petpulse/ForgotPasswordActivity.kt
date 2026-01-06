package com.example.petpulse

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.petpulse.viewmodel.AuthViewModel

class ForgotPasswordActivity : AppCompatActivity() {

    private val viewModel: AuthViewModel by viewModels()
    private lateinit var etEmail: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_forgot_password)

        etEmail = findViewById(R.id.etEmail)

        findViewById<Button>(R.id.btnSendOtp).setOnClickListener {
            val email = etEmail.text.toString().trim()

            if (email.isEmpty()) {
                Toast.makeText(this, "Enter email", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.forgotPassword(email)
        }

        viewModel.forgotResult.observe(this) { result ->
            result.onSuccess { response ->
                val msg = response.message
                Toast.makeText(this, msg, Toast.LENGTH_LONG).show()

                val intent = Intent(this, VerifyForgotOtpActivity::class.java)
                intent.putExtra("email", etEmail.text.toString().trim())
                startActivity(intent)
            }.onFailure { error ->
                Toast.makeText(this, "Failed: ${error.message}", Toast.LENGTH_LONG).show()
            }
        }
    }
}
