package com.example.petpulse

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.KeyEvent
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.petpulse.model.GeneralResponse
import com.example.petpulse.viewmodel.AuthViewModel

class VerifyForgotOtpActivity : AppCompatActivity() {
    private val viewModel: AuthViewModel by viewModels()
    private lateinit var otpBoxes: List<EditText>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup_otp) // We can reuse the layout for now

        val email = intent.getStringExtra("email") ?: run {
            finish()
            return
        }

        otpBoxes = listOf(
            findViewById(R.id.otp1),
            findViewById(R.id.otp2),
            findViewById(R.id.otp3),
            findViewById(R.id.otp4),
            findViewById(R.id.otp5),
            findViewById(R.id.otp6)
        )

        setupOtpInputs()

        findViewById<Button>(R.id.btnVerifyOtp).setOnClickListener {
            val otp = getEnteredOtp()
            if (otp.length == 6) {
                viewModel.verifyResetOtp(email, otp)
            } else {
                showError("Please enter a valid 6-digit OTP")
            }
        }

        viewModel.otpResult.observe(this) { result: Result<GeneralResponse> ->
            result.onSuccess { response ->
                if (response.status == "success") {
                    Toast.makeText(this, response.message, Toast.LENGTH_LONG).show()
                    val intent = Intent(this, ResetPasswordActivity::class.java)
                    intent.putExtra("email", email)
                    intent.putExtra("otp", getEnteredOtp())
                    startActivity(intent)
                } else {
                    showError(response.message)
                }
            }.onFailure { error ->
                showError(error.message ?: "OTP verification failed")
            }
        }
    }

    private fun setupOtpInputs() {
        for (i in otpBoxes.indices) {
            otpBoxes[i].addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

                override fun afterTextChanged(s: Editable?) {
                    if (s?.length == 1 && i < otpBoxes.size - 1) {
                        otpBoxes[i + 1].requestFocus()
                    }
                }
            })

            otpBoxes[i].setOnKeyListener { _, keyCode, event ->
                if (keyCode == KeyEvent.KEYCODE_DEL && event.action == KeyEvent.ACTION_DOWN && otpBoxes[i].text.isEmpty() && i > 0) {
                    otpBoxes[i - 1].requestFocus()
                    otpBoxes[i - 1].setSelection(otpBoxes[i - 1].text.length)
                }
                false
            }
        }
    }

    private fun getEnteredOtp(): String {
        return otpBoxes.joinToString("") { it.text.toString() }
    }

    private fun showError(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_LONG).show()
    }
}
