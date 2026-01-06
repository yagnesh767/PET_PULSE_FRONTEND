package com.example.petpulse

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.petpulse.databinding.ActivityWelcome1Binding

class WelcomeActivity1 : AppCompatActivity() {

    private lateinit var binding: ActivityWelcome1Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcome1Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.setOnClickListener {
            startActivity(Intent(this, WelcomeActivity2::class.java))
        }

        binding.btnSkip.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }
    }
}