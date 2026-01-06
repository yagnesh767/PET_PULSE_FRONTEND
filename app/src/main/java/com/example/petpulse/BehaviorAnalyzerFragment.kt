package com.example.petpulse

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment

class BehaviorAnalyzerFragment : Fragment() {

    private lateinit var mediaPreview: ImageView
    private lateinit var removeMediaButton: ImageView
    private lateinit var analyzeButton: Button
    private var mediaUri: Uri? = null

    // Activity result launcher for picking media
    private val pickMedia = registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            result.data?.data?.also {
                mediaUri = it
                mediaPreview.setImageURI(it)
                removeMediaButton.visibility = View.VISIBLE
                analyzeButton.isEnabled = true
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_behavior_analyzer, container, false)

        // Initialize views
        val backButton = view.findViewById<ImageView>(R.id.back_button)
        val uploadButton = view.findViewById<Button>(R.id.upload_button)
        mediaPreview = view.findViewById(R.id.media_preview)
        removeMediaButton = view.findViewById(R.id.remove_media_button)
        analyzeButton = view.findViewById(R.id.analyze_button)

        // Set initial state
        analyzeButton.isEnabled = false

        // Set click listeners
        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        uploadButton.setOnClickListener {
            val intent = Intent(Intent.ACTION_OPEN_DOCUMENT).apply {
                addCategory(Intent.CATEGORY_OPENABLE)
                type = "*/*"
                putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*", "video/*"))
            }
            pickMedia.launch(intent)
        }

        removeMediaButton.setOnClickListener {
            mediaUri = null
            mediaPreview.setImageURI(null)
            removeMediaButton.visibility = View.GONE
            analyzeButton.isEnabled = false
        }

        analyzeButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, BehaviorAnalysisResultFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }
}