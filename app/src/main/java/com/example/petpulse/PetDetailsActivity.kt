package com.example.petpulse

import android.Manifest
import android.content.ContentValues
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.viewpager2.widget.ViewPager2
import com.example.petpulse.network.ApiClient
import com.example.petpulse.adapter.ViewPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import okhttp3.ResponseBody
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class PetDetailsActivity : AppCompatActivity() {

    private lateinit var petImageView: ImageView
    private var imageUri: Uri? = null

    // State for sharing
    private var currentPetName = ""
    private var currentPetBreed = ""
    private var currentPetAge = 0
    private var currentPetWeight = 0.0

    private val requestCameraPermissionLauncher = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        if (isGranted) {
            openCamera()
        } else {
            Toast.makeText(this, "Camera permission is required to take photos", Toast.LENGTH_LONG).show()
        }
    }

    private val takePictureLauncher = registerForActivityResult(
        ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            imageUri?.let {
                // Upload immediately
                val petId = intent.getIntExtra("pet_id", -1)
                if (petId != -1) {
                    uploadPetImage(it, petId)
                } else {
                    // Fallback if intent didn't have it, try prefs or just set image
                    val prefs = getSharedPreferences("user_session", android.content.Context.MODE_PRIVATE)
                    val prefPetId = prefs.getInt("selected_pet_id", -1)
                    if (prefPetId != -1) {
                        uploadPetImage(it, prefPetId)
                    } else {
                        petImageView.setImageURI(it)
                    }
                }
            }
        }
    }

    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            imageUri = it
            var petId = intent.getIntExtra("pet_id", -1)
            
            if (petId == -1) {
                val prefs = getSharedPreferences("user_session", android.content.Context.MODE_PRIVATE)
                petId = prefs.getInt("selected_pet_id", -1)
            }

            if (petId != -1) {
                uploadPetImage(it, petId)
            } else {
                petImageView.setImageURI(it)
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pet_details)

        petImageView = findViewById(R.id.pet_image)
        val viewPager = findViewById<ViewPager2>(R.id.viewPager)
        val tabLayout = findViewById<TabLayout>(R.id.tabLayout)
        val backButton = findViewById<ImageView>(R.id.backButton)
        val cameraIcon = findViewById<ImageView>(R.id.camera_icon)
        val shareButton = findViewById<ImageView>(R.id.shareButton)

        backButton.setOnClickListener { 
            finish()
        }

        cameraIcon.setOnClickListener {
            showImageSourceDialog()
        }

        shareButton.setOnClickListener {
            sharePetInfo()
        }

        // Pet Selection Logic: Intent > SharedPreferences
        var petId = intent.getIntExtra("pet_id", -1)
        if (petId == -1) {
             val prefs = getSharedPreferences("user_session", android.content.Context.MODE_PRIVATE)
             petId = prefs.getInt("selected_pet_id", -1)
        }

        val adapter = ViewPagerAdapter(this)
        viewPager.adapter = adapter
        
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Overview"
                1 -> "Medical"
                2 -> "Vaccines"
                3 -> "Weight"
                4 -> "Diet"
                5 -> "Activity"
                else -> null
            }
        }.attach()

        val selectedTab = intent.getIntExtra("SELECTED_TAB", 0)
        viewPager.setCurrentItem(selectedTab, false)

        if (petId != -1) {
            loadPetDetails(petId)
        } else {
            Toast.makeText(this, "No pet selected", Toast.LENGTH_SHORT).show()
        }
    }

    private fun loadPetDetails(petId: Int) {
        val call = ApiClient.apiService.getPetById(petId)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        val jsonString = response.body()!!.string()
                        val json = JSONObject(jsonString)
                        if (json.getString("status") == "success") {
                            val petObj = json.getJSONObject("pet")
                            
                            // Store in properties for sharing
                            currentPetName = petObj.getString("name")
                            currentPetBreed = petObj.optString("breed", "Unknown")
                            currentPetAge = petObj.optInt("age", 0)
                            currentPetWeight = petObj.optDouble("weight", 0.0)

                            val photo = if (petObj.has("photo") && !petObj.isNull("photo")) {
                                petObj.getString("photo")
                            } else if (petObj.has("image_url") && !petObj.isNull("image_url")) {
                                petObj.getString("image_url")
                            } else {
                                ""
                            }
                            
                            findViewById<TextView>(R.id.pet_name)?.text = currentPetName
                            findViewById<TextView>(R.id.tvPetBreedAge)?.text = "$currentPetBreed • $currentPetAge years"

                            if (photo.isNotEmpty()) {
                                val fullUrl = ApiClient.getFullImageUrl(photo)
                                com.bumptech.glide.Glide.with(this@PetDetailsActivity)
                                    .load(fullUrl)
                                    .placeholder(R.drawable.dog_placeholder)
                                    .centerCrop()
                                    .into(petImageView)
                            }
                        }
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@PetDetailsActivity, "Failed to load pet details", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun sharePetInfo() {
        if (currentPetName.isEmpty()) {
             Toast.makeText(this, "Pet details not loaded yet", Toast.LENGTH_SHORT).show()
             return
        }
        
        val shareText = """
        Pet Name: $currentPetName
        Breed: $currentPetBreed
        Age: $currentPetAge
        Weight: $currentPetWeight kg
        """.trimIndent()

        val intent = Intent(Intent.ACTION_SEND)
        intent.type = "text/plain"
        intent.putExtra(Intent.EXTRA_TEXT, shareText)
        startActivity(Intent.createChooser(intent, "Share Pet Info"))
    }

    private fun uploadPetImage(uri: Uri, petId: Int) {
        val file = getFileFromUri(uri) ?: return

        val requestFile = file.asRequestBody("image/*".toMediaTypeOrNull())
        val body = okhttp3.MultipartBody.Part.createFormData("image", file.name, requestFile)
        val petIdBody = petId.toString().toRequestBody("text/plain".toMediaTypeOrNull())

        val call = ApiClient.apiService.updatePetImage(petIdBody, body)
        call.enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        val jsonString = response.body()!!.string()
                        val json = JSONObject(jsonString)
                         if (json.getString("status") == "success") {
                             val newUrl = json.getString("image_url") 
                             val fullUrl = ApiClient.getFullImageUrl(newUrl)
                             
                             com.bumptech.glide.Glide.with(this@PetDetailsActivity)
                                 .load(fullUrl)
                                 .placeholder(R.drawable.dog_placeholder)
                                 .centerCrop()
                                 .into(petImageView)
                                 
                             Toast.makeText(this@PetDetailsActivity, "Image updated!", Toast.LENGTH_SHORT).show()
                         }
                    } catch (e: Exception) {
                        e.printStackTrace()
                        Toast.makeText(this@PetDetailsActivity, "Error parsing response", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Toast.makeText(this@PetDetailsActivity, "Upload failed", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Toast.makeText(this@PetDetailsActivity, "Network error", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun getFileFromUri(uri: Uri): java.io.File? {
        val contentResolver = contentResolver
        val tempFile = java.io.File(cacheDir, "temp_image_${System.currentTimeMillis()}.jpg")
        try {
            val inputStream = contentResolver.openInputStream(uri) ?: return null
            val outputStream = java.io.FileOutputStream(tempFile)
            inputStream.copyTo(outputStream)
            inputStream.close()
            outputStream.close()
            return tempFile
        } catch (e: Exception) {
            e.printStackTrace()
            return null
        }
    }

    private fun showImageSourceDialog() {
        val options = arrayOf("Take Photo", "Choose from Gallery")
        AlertDialog.Builder(this)
            .setTitle("Select Image Source")
            .setItems(options) { dialog, which ->
                when (which) {
                    0 -> checkCameraPermissionAndOpenCamera()
                    1 -> openGallery()
                }
            }
            .show()
    }

    private fun checkCameraPermissionAndOpenCamera() {
        when {
            ContextCompat.checkSelfPermission(
                this,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                openCamera()
            }
            shouldShowRequestPermissionRationale(Manifest.permission.CAMERA) -> {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
            else -> {
                requestCameraPermissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    private fun openCamera() {
        val values = ContentValues().apply {
            put(MediaStore.Images.Media.TITLE, "New Pet Picture")
            put(MediaStore.Images.Media.DESCRIPTION, "From your Camera")
        }
        imageUri = contentResolver.insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values)
        takePictureLauncher.launch(imageUri)
    }

    private fun openGallery() {
        pickImageLauncher.launch("image/*")
    }
}
