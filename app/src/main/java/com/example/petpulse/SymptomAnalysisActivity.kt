package com.example.petpulse

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class SymptomAnalysisActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_symptom_analysis)

        val backButton = findViewById<ImageView>(R.id.back_button)
        val summaryTextView = findViewById<TextView>(R.id.summary_text)
        val concernTextView = findViewById<TextView>(R.id.concern_text)
        val chatButton = findViewById<Button>(R.id.chat_button)
        val findVetButton = findViewById<Button>(R.id.find_vet_button)

        backButton.setOnClickListener {
            finish()
        }

        chatButton.setOnClickListener {
            startActivity(Intent(this, AiVetChatActivity::class.java))
        }

        findVetButton.setOnClickListener {
            startActivity(Intent(this, FindAVetActivity::class.java))
        }

        val q1Answer = intent.getIntExtra("q1_answer", -1)
        val q2Answer = intent.getIntExtra("q2_answer", -1)
        val q3Answer = intent.getIntExtra("q3_answer", -1)
        val q4Answer = intent.getIntExtra("q4_answer", -1)

        // Corrected analysis logic - compares simple integers, not resource IDs
        val concernLevel = if (q1Answer == 3 || q2Answer == 3 || q3Answer == 1 || q4Answer == 3) {
            "High"
        } else if (q1Answer == 2 || q2Answer == 2 || q4Answer == 2) {
            "Moderate"
        } else {
            "Low"
        }

        summaryTextView.text = "Your responses have been recorded."
        concernTextView.text = "Concern Level: $concernLevel"
    }
}
