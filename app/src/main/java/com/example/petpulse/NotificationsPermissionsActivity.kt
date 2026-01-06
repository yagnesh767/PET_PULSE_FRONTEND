package com.example.petpulse

import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class NotificationsPermissionsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_notifications_permissions)

        findViewById<ImageView>(R.id.btnBack).setOnClickListener {
            finish() // Go back to the previous screen (HomeActivity)
        }

        findViewById<Button>(R.id.allow_button).setOnClickListener {
            // TODO: Save permission preferences
            finish() // Go back to the previous screen (HomeActivity)
        }

        findViewById<TextView>(R.id.maybe_later_button).setOnClickListener {
            finish() // Go back to the previous screen (HomeActivity)
        }
    }
}
