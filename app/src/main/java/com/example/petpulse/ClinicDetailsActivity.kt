package com.example.petpulse

import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class ClinicDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_clinic_details)

        val backButton = findViewById<ImageView>(R.id.backButton)
        val openMapsButton = findViewById<Button>(R.id.open_maps_button)
        val bookAppointmentButton = findViewById<Button>(R.id.book_appointment_button)

        backButton.setOnClickListener {
            finish()
        }

        openMapsButton.setOnClickListener {
            // Coordinates for "City Pet Clinic"
            openGoogleMaps(40.7128, -74.0060, "City Pet Clinic")
        }

        bookAppointmentButton.setOnClickListener {
            startActivity(Intent(this, BookAppointmentActivity::class.java))
        }
    }

    private fun openGoogleMaps(lat: Double, lng: Double, name: String) {
        val uri = Uri.parse("google.navigation:q=$lat,$lng&mode=d")
        val intent = Intent(Intent.ACTION_VIEW, uri)
        intent.setPackage("com.google.android.apps.maps")

        if (intent.resolveActivity(packageManager) != null) {
            startActivity(intent)
        } else {
            // Fallback to browser if Google Maps is not installed
            val webUri = Uri.parse("https://www.google.com/maps/dir/?api=1&destination=$lat,$lng")
            startActivity(Intent(Intent.ACTION_VIEW, webUri))
        }
    }
}
