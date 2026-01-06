package com.example.petpulse

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.RelativeLayout
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class BookAppointmentActivity : AppCompatActivity() {

    private lateinit var dateText: TextView
    private lateinit var timeText: TextView
    private val calendar = Calendar.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_book_appointment)

        val backButton = findViewById<ImageView>(R.id.backButton)
        val confirmButton = findViewById<Button>(R.id.confirm_booking_button)
        dateText = findViewById(R.id.date_text)
        timeText = findViewById(R.id.time_text)
        val datePickerContainer = findViewById<RelativeLayout>(R.id.date_picker_container)
        val timePickerContainer = findViewById<RelativeLayout>(R.id.time_picker_container)

        updateDateInView()
        updateTimeInView()

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
            DatePickerDialog(this, dateSetListener, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH)).show()
        }

        val timeSetListener = TimePickerDialog.OnTimeSetListener { _, hourOfDay, minute ->
            calendar.set(Calendar.HOUR_OF_DAY, hourOfDay)
            calendar.set(Calendar.MINUTE, minute)
            updateTimeInView()
        }

        timePickerContainer.setOnClickListener {
            TimePickerDialog(this, timeSetListener, calendar.get(Calendar.HOUR_OF_DAY), calendar.get(Calendar.MINUTE), false).show()
        }

        confirmButton.setOnClickListener {
            showConfirmationBottomSheet()
        }
    }

    private fun showConfirmationBottomSheet() {
        val bottomSheetView = layoutInflater.inflate(R.layout.bottom_sheet_booking_confirmation, null)
        val dialog = BottomSheetDialog(this)
        dialog.setContentView(bottomSheetView)

        val closeButton = bottomSheetView.findViewById<ImageView>(R.id.close_button)
        val confirmationDetails = bottomSheetView.findViewById<TextView>(R.id.confirmation_details)

        val sdfDate = SimpleDateFormat("EEE, MMM dd", Locale.US)
        val sdfTime = SimpleDateFormat("h:mm a", Locale.US)
        confirmationDetails.text = "See you on ${sdfDate.format(calendar.time)}, ${sdfTime.format(calendar.time)}."

        closeButton.setOnClickListener {
            dialog.dismiss()
            finish()
        }

        dialog.show()
    }

    private fun updateDateInView() {
        val myFormat = "MMM dd, yyyy"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        dateText.text = sdf.format(calendar.time)
    }

    private fun updateTimeInView() {
        val myFormat = "h:mm a"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        timeText.text = sdf.format(calendar.time)
    }
}
