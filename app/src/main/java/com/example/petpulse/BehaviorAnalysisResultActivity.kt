package com.example.petpulse

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class BehaviorAnalysisResultActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_behavior_analysis_result)

        val backButton = findViewById<ImageView>(R.id.back_button)

        backButton.setOnClickListener {
            finish()
        }
    }
}
