package com.example.petpulse

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import androidx.appcompat.app.AppCompatActivity

class SplashActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        Handler(Looper.getMainLooper()).postDelayed({
            val prefs = getSharedPreferences("user_session", MODE_PRIVATE)
            val isLoggedIn = prefs.getBoolean("is_logged_in", false)
            if (isLoggedIn) {
                startActivity(Intent(this, MainActivity::class.java))
            } else {
                startActivity(Intent(this, WelcomeActivity1::class.java))
            }
            finish()
        }, 2000)
    }
}