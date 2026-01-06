package com.example.petpulse

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.petpulse.viewmodel.AuthViewModel

class ResetPasswordActivity : AppCompatActivity() {

    private val viewModel: AuthViewModel by viewModels()
    private lateinit var etNewPassword: EditText
    private lateinit var etConfirmPassword: EditText

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_reset_password)

        etNewPassword = findViewById(R.id.etNewPassword)
        etConfirmPassword = findViewById(R.id.etConfirmPassword)

        val email = intent.getStringExtra("email")
        val otp = intent.getStringExtra("otp")

        if (email.isNullOrEmpty() || otp.isNullOrEmpty()) {
            Toast.makeText(this, "Invalid request", Toast.LENGTH_LONG).show()
            finish()
            return
        }

        findViewById<Button>(R.id.btnResetPassword).setOnClickListener {
            val newPassword = etNewPassword.text.toString().trim()
            val confirmPassword = etConfirmPassword.text.toString().trim()

            if (newPassword.isEmpty() || confirmPassword.isEmpty()) {
                Toast.makeText(this, "Please enter and confirm your new password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (newPassword != confirmPassword) {
                Toast.makeText(this, "Passwords do not match", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.resetPassword(email, otp, newPassword)
        }

        viewModel.resetPasswordResult.observe(this) { result ->
            result.onSuccess { response ->
                Toast.makeText(this, response.message, Toast.LENGTH_LONG).show()
                if (response.status == "success") {
                    val intent = Intent(this, LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finishAffinity()
                }
            }.onFailure { error ->
                Toast.makeText(this, error.message ?: "Password reset failed", Toast.LENGTH_LONG).show()
            }
        }
    }
}
