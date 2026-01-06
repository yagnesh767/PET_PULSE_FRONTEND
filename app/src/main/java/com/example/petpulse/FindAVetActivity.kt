package com.example.petpulse

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class FindAVetActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_find_a_vet)

        val backButton = findViewById<ImageView>(R.id.backButton)
        val viewOnMapButton = findViewById<Button>(R.id.view_on_map_button)
        val bookButton = findViewById<Button>(R.id.book_button)

        backButton.setOnClickListener {
            finish()
        }

        viewOnMapButton.setOnClickListener {
            // Create a Uri from an intent string. Use the result to create an Intent.
            val gmmIntentUri = Uri.parse("geo:0,0?q=veterinary clinics near me")
            val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
            startActivity(mapIntent)
        }

        bookButton.setOnClickListener {
            val intent = Intent(this, BookAppointmentActivity::class.java)
            startActivity(intent)
        }
    }
}
