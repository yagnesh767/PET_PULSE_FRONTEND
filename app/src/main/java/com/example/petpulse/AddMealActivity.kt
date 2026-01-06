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

class AddMealActivity : AppCompatActivity() {

    private lateinit var timeText: TextView
    private lateinit var filePreviewIcon: ImageView
    private lateinit var filePreviewText: TextView
    private val calendar = Calendar.getInstance()

    private val openDocumentLauncher = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val uri = result.data?.data
            if (uri != null) {
                contentResolver.query(uri, null, null, null, null)?.use { cursor ->
                    val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
                    cursor.moveToFirst()
                    val fileName = cursor.getString(nameIndex)
                    filePreviewText.text = fileName
                }

                if (uri.toString().contains("image")) {
                    filePreviewIcon.setImageURI(uri)
                } else {
                    filePreviewIcon.setImageResource(R.drawable.ic_document)
                }
            } else {
                filePreviewText.text = "No photo selected"
                filePreviewIcon.setImageResource(R.drawable.ic_upload)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_meal)

        val backButton = findViewById<ImageView>(R.id.backButton)
        timeText = findViewById(R.id.time_text)
        val timePickerContainer = findViewById<RelativeLayout>(R.id.time_picker_container)
        val uploadButton = findViewById<Button>(R.id.upload_photo_button)
        val saveButton = findViewById<Button>(R.id.save_meal_button)
        filePreviewIcon = findViewById(R.id.file_preview_icon)
        filePreviewText = findViewById(R.id.file_preview_text)

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

        uploadButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "*/*"
            val mimeTypes = arrayOf("image/*", "application/pdf")
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            openDocumentLauncher.launch(intent)
        }

        saveButton.setOnClickListener {
            Toast.makeText(this, "Meal logged", Toast.LENGTH_SHORT).show()
            finish()
        }
    }

    private fun updateTimeInView() {
        val myFormat = "h:mm a"
        val sdf = SimpleDateFormat(myFormat, Locale.US)
        timeText.text = sdf.format(calendar.time)
    }
}
