package com.example.petpulse

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.bottomnavigation.BottomNavigationView

class BehaviorAnalyzerActivity : AppCompatActivity() {

    private lateinit var mediaPreview: ImageView
    private lateinit var removeMediaButton: ImageView
    private lateinit var analyzeButton: Button
    private var mediaUri: Uri? = null

    private val pickMedia = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.also {
                mediaUri = it
                mediaPreview.setImageURI(it)
                removeMediaButton.visibility = ImageView.VISIBLE
                analyzeButton.isEnabled = true
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_behavior_analyzer)

        val backButton = findViewById<ImageView>(R.id.back_button)
        val uploadButton = findViewById<Button>(R.id.upload_button)
        mediaPreview = findViewById(R.id.media_preview)
        removeMediaButton = findViewById(R.id.remove_media_button)
        analyzeButton = findViewById(R.id.analyze_button)
        val bottomNavigation = findViewById<BottomNavigationView>(R.id.bottom_navigation)

        bottomNavigation.selectedItemId = R.id.nav_ai

        backButton.setOnClickListener {
            finish()
        }

        uploadButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT)
            intent.addCategory(Intent.CATEGORY_OPENABLE)
            intent.type = "*/*"
            val mimeTypes = arrayOf("image/*", "video/*")
            intent.putExtra(Intent.EXTRA_MIME_TYPES, mimeTypes)
            pickMedia.launch(intent)
        }

        removeMediaButton.setOnClickListener {
            mediaUri = null
            mediaPreview.setImageURI(null)
            mediaPreview.setBackgroundColor(getColor(android.R.color.white))
            removeMediaButton.visibility = ImageView.GONE
            analyzeButton.isEnabled = false
        }

        analyzeButton.setOnClickListener {
            val intent = Intent(this, BehaviorAnalysisResultActivity::class.java)
            intent.data = mediaUri
            startActivity(intent)
        }
    }
}
