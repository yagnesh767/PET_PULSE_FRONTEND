package com.example.petpulse

import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.petpulse.databinding.ActivityWelcome2Binding

class WelcomeActivity2 : AppCompatActivity() {

    private lateinit var binding: ActivityWelcome2Binding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWelcome2Binding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.btnNext.setOnClickListener {
            startActivity(Intent(this, WelcomeActivity3::class.java))
        }

        binding.btnSkip.setOnClickListener {
            startActivity(Intent(this, LoginActivity::class.java))
            finishAffinity()
        }
    }
}