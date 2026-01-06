package com.example.petpulse

import android.app.DatePickerDialog
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class AddWeightLogActivity : AppCompatActivity() {

    private lateinit var dateText: TextView
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_weight_log)

        val backButton = findViewById<ImageView>(R.id.backButton)
        dateText = findViewById(R.id.date_text)
        val datePickerContainer = findViewById<RelativeLayout>(R.id.date_picker_container)
        val saveButton = findViewById<Button>(R.id.save_weight_button) // Correct ID

        updateDateInView()

        backButton.setOnClickListener {
            finish()
        }

        val dateSetListener = DatePickerDialog.OnDateSetListener { _, year, month, dayOfMonth ->
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            updateDateInView()
        }

        datePickerContainer.setOnClickListener {
            DatePickerDialog(
                this,
                dateSetListener,
                calendar.get(Calendar.YEAR),
                calendar.get(Calendar.MONTH),
                calendar.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        saveButton.setOnClickListener {
            Toast.makeText(this, "Weight saved", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun updateDateInView() {
        val myFormat = "MMM dd, yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        dateText.text = sdf.format(calendar.time)
    }
}
