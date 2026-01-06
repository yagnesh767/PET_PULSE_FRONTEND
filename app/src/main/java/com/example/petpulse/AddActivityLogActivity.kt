package com.example.petpulse

import android.app.Activity
import android.app.TimePickerDialog
import android.content.Intent
import android.os.Bundle
import android.provider.OpenableColumns
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddActivityLogActivity : AppCompatActivity() {

    private lateinit var timeText: TextView
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_activity_log)

        val backButton = findViewById<ImageView>(R.id.backButton)
        timeText = findViewById(R.id.time_text)
        val timePickerContainer = findViewById<RelativeLayout>(R.id.time_picker_container)
        val saveButton = findViewById<Button>(R.id.save_activity_button)

        updateTimeInView()

        backButton.setOnClickListener {
            finish()
        }

        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            updateTimeInView()
        }

        timePickerContainer.setOnClickListener {
            TimePickerDialog(
                this,
                timeSetListener,
                calendar.get(Calendar.HOUR_OF_DAY),
                calendar.get(Calendar.MINUTE),
                false
            ).show()
        }

        saveButton.setOnClickListener {
            Toast.makeText(this, "Activity logged", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun updateTimeInView() {
        val myFormat = "h:mm a"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        timeText.text = sdf.format(calendar.time)
    }
}
