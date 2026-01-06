package com.example.petpulse

import android.app.Activity
import android.app.DatePickerDialog
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import com.example.petpulse.model.GeneralResponse
import com.example.petpulse.network.ApiClient
import com.example.petpulse.network.ApiService
import okhttp3.MultipartBody
import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import com.example.petpulse.utils.extractMessage
import java.io.File
import java.io.FileOutputStream
import java.text.SimpleDateFormat
import java.util.*
import okhttp3.MediaType
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.RequestBody.Companion.asRequestBody

class AddMedicalRecordActivity : AppCompatActivity() {

    private var selectedFileUri: Uri? = null
    private var petId = -1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_medical_record)

        petId = intent.getIntExtra("pet_id", -1)
        if (petId == -1) {
            Toast.makeText(this, "Pet not selected", Toast.LENGTH_SHORT).show()
            finish()
            return
        }

        val etTitle = findViewById<EditText>(R.id.etCondition)
        val etDate = findViewById<EditText>(R.id.date_picker)
        val etVet = findViewById<EditText>(R.id.etVetName)
        val btnUpload = findViewById<Button>(R.id.upload_button)
        val btnSave = findViewById<Button>(R.id.save_record_button)

        // Date picker (FIXES EMPTY DATE BUG)
        etDate.setOnClickListener {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                this,
                { _, y, m, d ->
                    etDate.setText("$d/${m + 1}/$y")
                },
                cal.get(Calendar.YEAR),
                cal.get(Calendar.MONTH),
                cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }

        btnUpload.setOnClickListener {
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "*/*"
            startActivityForResult(intent, 101)
        }

        btnSave.setOnClickListener {

            val title = etTitle.text.toString().trim()
            val rawDate = etDate.text.toString().trim()
            val vet = etVet.text.toString().trim()

            if (title.isEmpty() || rawDate.isEmpty()) {
                Toast.makeText(this, "Title and Date required", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            val formattedDate = try {
                val inFmt = SimpleDateFormat("d/M/yyyy", Locale.getDefault())
                val outFmt = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
                outFmt.format(inFmt.parse(rawDate)!!)
            } catch (e: Exception) {
                Toast.makeText(this, "Invalid date", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            // Backend ENUM-safe value
            val recordType = "Medical"

            Log.d("ADD_MEDICAL", "pet=$petId type=$recordType title=$title date=$formattedDate vet=$vet")

            // The PHP backend likely expects multipart form data without a content-type
            // for text fields to correctly populate the $_POST array.
            val textType: MediaType? = null

            val petIdBody = petId.toString().toRequestBody(textType)
            val recordTypeBody = recordType.toRequestBody(textType)
            val titleBody = title.toRequestBody(textType)
            val dateBody = formattedDate.toRequestBody(textType)
            val vetBody = vet.toRequestBody(textType)

            var filePart: MultipartBody.Part? = null
            selectedFileUri?.let {
                val file = uriToFile(it)
                val fileBody = file.asRequestBody("application/octet-stream".toMediaType())
                filePart = MultipartBody.Part.createFormData("file", file.name, fileBody)
            }

            val api = ApiClient.apiService

            api.addMedicalRecord(
                petIdBody,
                recordTypeBody,
                titleBody,
                dateBody,
                vetBody,
                filePart
            ).enqueue(object : Callback<GeneralResponse> {

                override fun onResponse(call: Call<GeneralResponse>, response: Response<GeneralResponse>) {
                    // Using the safe extractor as recommended
                    Toast.makeText(
                        this@AddMedicalRecordActivity,
                        extractMessage(response),
                        Toast.LENGTH_SHORT
                    ).show()

                    if (response.isSuccessful && response.body()?.status == "success") {
                        finish()
                    }
                }

                override fun onFailure(call: Call<GeneralResponse>, t: Throwable) {
                    Log.e("ADD_MEDICAL", "Network error", t)
                    Toast.makeText(this@AddMedicalRecordActivity, "Network error", Toast.LENGTH_SHORT).show()
                }
            })
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 101 && resultCode == Activity.RESULT_OK) {
            selectedFileUri = data?.data
        }
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
}
