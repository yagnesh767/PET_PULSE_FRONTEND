package com.example.petpulse

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.view.View
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.petpulse.network.ApiClient
import com.example.petpulse.network.ApiService
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import com.example.petpulse.model.GeneralResponse
import com.example.petpulse.utils.extractMessage
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*

class AddVaccineActivity : AppCompatActivity() {

    private var petId = -1
    private var selectedFileUri: Uri? = null

    // View references
    private lateinit var etVaccineName: EditText
    private lateinit var etVaccineDate: EditText
    private lateinit var etNextDue: EditText
    private lateinit var radio_completed: RadioButton
    private lateinit var save_vaccine_button: Button
    private lateinit var upload_button: Button
    private lateinit var file_preview_text: TextView
    private lateinit var backButton: android.widget.ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_vaccine)

        petId = intent.getIntExtra("pet_id", -1)

        // Initialize Views
        etVaccineName = findViewById(R.id.etVaccineName)
        etVaccineDate = findViewById(R.id.vaccination_date_picker)
        etNextDue = findViewById(R.id.due_date_picker)
        radio_completed = findViewById(R.id.radio_completed)
        save_vaccine_button = findViewById(R.id.save_vaccine_button)
        upload_button = findViewById(R.id.upload_button)
        file_preview_text = findViewById(R.id.file_preview_text)
        backButton = findViewById(R.id.backButton)

        backButton.setOnClickListener { finish() }

        etVaccineDate.setOnClickListener { showDatePickerDialog(etVaccineDate) }
        etNextDue.setOnClickListener { showDatePickerDialog(etNextDue) }

        upload_button.setOnClickListener { openFilePicker() }
        save_vaccine_button.setOnClickListener { submit() }
    }

    private fun openFilePicker() {
        val intent = Intent(Intent.ACTION_GET_CONTENT)
        intent.type = "*/*"
        startActivityForResult(intent, 101)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            data?.data?.let {
                selectedFileUri = it
                file_preview_text.text = getFileName(it)
            }
        }
    }

    private fun getFileName(uri: Uri): String {
        var result: String? = null
        if (uri.scheme == "content") {
            val cursor = contentResolver.query(uri, null, null, null, null)
            try {
                if (cursor != null && cursor.moveToFirst()) {
                    result = cursor.getString(cursor.getColumnIndexOrThrow(OpenableColumns.DISPLAY_NAME))
                }
            } finally {
                cursor?.close()
            }
        }
        if (result == null) {
            result = uri.path
            val cut = result?.lastIndexOf('/')
            if (cut != -1) {
                if (cut != null) {
                    result = result?.substring(cut + 1)
                }
            }
        }
        return result ?: "No file selected"
    }

    private fun showDatePickerDialog(editText: EditText) {
        val calendar = java.util.Calendar.getInstance()
        DatePickerDialog(
            this,
            { _, year, month, day ->
                val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                val selectedDate = Calendar.getInstance()
                selectedDate.set(year, month, day)
                editText.setText(sdf.format(selectedDate.time))
            },
            calendar.get(java.util.Calendar.YEAR),
            calendar.get(java.util.Calendar.MONTH),
            calendar.get(java.util.Calendar.DAY_OF_MONTH)
        ).show()
    }

    private fun submit() {
        val vaccineName = etVaccineName.text.toString().trim()
        val vaccinationDate = etVaccineDate.text.toString().trim()
        val nextDueDate = etNextDue.text.toString().trim()
        val status = if (radio_completed.isChecked) "Completed" else "Due Soon"

        if (vaccineName.isEmpty() || vaccinationDate.isEmpty() || petId == -1) {
            toast("Fill all required fields")
            return
        }

        val petIdBody = petId.toString().toRequestBody("text/plain".toMediaType())
        val titleBody = vaccineName.toRequestBody("text/plain".toMediaType())
        val dateBody = vaccinationDate.toRequestBody("text/plain".toMediaType())
        val statusBody = status.toRequestBody("text/plain".toMediaType())
        val nextDueBody = nextDueDate.toRequestBody("text/plain".toMediaType())

        var filePart: MultipartBody.Part? = null
        selectedFileUri?.let {
            val file = uriToFile(it)
            val fileBody = file.asRequestBody("application/octet-stream".toMediaType())
            filePart = MultipartBody.Part.createFormData("file", file.name, fileBody)
        }

        val api = ApiClient.apiService
        api.addVaccine(petIdBody, titleBody, dateBody, statusBody, nextDueBody, filePart)
            .enqueue(object : Callback<GeneralResponse> {
                override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                    Toast.makeText(this@AddVaccineActivity, extractMessage(response), Toast.LENGTH_SHORT).show()
                    
                    // Allow lenient success check if status is success OR if we just want to proceed on 200 OK with assumptions
                    // But better: check status if available.
                    if (response.isSuccessful && (response.body()?.status == "success" || response.body()?.message?.contains("success", true) == true)) {
                         finish()
                    }
                }

                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    toast("Network error: ${t.message}")
                }
            })
    }

    private fun uriToFile(uri: Uri): File {
        val cursor = contentResolver.query(uri, null, null, null, null)
        val nameIndex = cursor!!.getColumnIndex(OpenableColumns.DISPLAY_NAME)
        cursor.moveToFirst()
        val name = cursor.getString(nameIndex)
        cursor.close()

        val file = File(cacheDir, name)
        val input = contentResolver.openInputStream(uri)!!
        val output = FileOutputStream(file)

        input.copyTo(output)
        input.close()
        output.close()

        return file
    }

    private fun toast(msg: String) =
        Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
}
