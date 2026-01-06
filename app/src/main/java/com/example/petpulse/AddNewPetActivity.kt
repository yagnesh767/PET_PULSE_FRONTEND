package com.example.petpulse

import android.net.Uri
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.RadioButton
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.petpulse.databinding.ActivityAddNewPetBinding
import com.example.petpulse.network.ApiClient
import com.example.petpulse.repository.PetRepository
import com.example.petpulse.utils.SessionManager
import com.example.petpulse.viewmodel.AddNewPetViewModel
import com.example.petpulse.viewmodel.AddNewPetViewModelFactory
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import java.io.FileOutputStream

class AddNewPetActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddNewPetBinding
    private val viewModel: AddNewPetViewModel by viewModels {
        AddNewPetViewModelFactory(PetRepository(ApiClient.apiService))
    }
    private var selectedImageUri: Uri? = null

    private val imagePickerLauncher = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        if (uri != null) {
            selectedImageUri = uri
            binding.petAvatar.setImageURI(uri)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddNewPetBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        binding.btnBack.setOnClickListener { finish() }

        val speciesAdapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, arrayOf("Dog", "Cat", "Other"))
        binding.spSpecies.adapter = speciesAdapter

        binding.cameraButton.setOnClickListener { imagePickerLauncher.launch("image/*") }
        binding.petAvatar.setOnClickListener { imagePickerLauncher.launch("image/*") }

        binding.btnSavePet.setOnClickListener {
            createPet()
        }

        viewModel.addPetResult.observe(this) { response ->
            if (response != null && response.status == "success") {
                Toast.makeText(this, "Pet Added Successfully!", Toast.LENGTH_SHORT).show()
                finish()
            } else {
                Toast.makeText(this, response?.message ?: "Failed to add pet", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun createPet() {
        val session = SessionManager(this)
        val userId = session.getUserId()

        val name = viewModel.petName.value ?: ""
        val species = binding.spSpecies.selectedItem?.toString() ?: "Other"
        val breed = viewModel.breed.value ?: ""
        val ageStr = viewModel.age.value ?: ""
        val weightStr = viewModel.weight.value ?: ""

        val selectedGenderId = binding.rgGender.checkedRadioButtonId
        val gender = if (selectedGenderId != -1) {
            findViewById<RadioButton>(selectedGenderId).text.toString()
        } else {
            "Unknown"
        }

        if (name.isEmpty() || ageStr.isEmpty() || weightStr.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
            return
        }

        val userIdBody = userId.toString().toRequestBody("text/plain".toMediaType())
        val petNameBody = name.toRequestBody("text/plain".toMediaType())
        val speciesBody = species.toRequestBody("text/plain".toMediaType())
        val breedBody = breed.toRequestBody("text/plain".toMediaType())
        val genderBody = gender.toRequestBody("text/plain".toMediaType())
        val ageBody = ageStr.toRequestBody("text/plain".toMediaType())
        val weightBody = weightStr.toRequestBody("text/plain".toMediaType())

        var imagePart: MultipartBody.Part? = null
        selectedImageUri?.let { uri ->
            val file = getFileFromUri(uri)
            if (file != null) {
                val req = file.asRequestBody("image/*".toMediaType())
                imagePart = MultipartBody.Part.createFormData("image", file.name, req)
            }
        }

        viewModel.addPet(
            userIdBody,
            petNameBody,
            speciesBody,
            breedBody,
            genderBody,
            ageBody,
            weightBody,
            imagePart
        )
    }

    private fun getFileFromUri(uri: Uri): File? {
        val contentResolver = contentResolver
        val tempFile = File(cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
        try {
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val outputStream = FileOutputStream(tempFile)
            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()
            return tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }
}
