package com.example.petpulse

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.petpulse.databinding.ActivityForgotPasswordOtpBinding

class ForgotPasswordOtpActivity : AppCompatActivity() {

    private lateinit var binding: ActivityForgotPasswordOtpBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityForgotPasswordOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnVerify.setOnClickListener {
            startActivity(Intent(this, ResetPasswordActivity::class.java))
            finish()
        }
    }
}
