package com.example.petpulse

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment

class BehaviorAnalysisResultFragment : Fragment(R.layout.fragment_behavior_analysis_result) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val backButton = view.findViewById<Button>(R.id.back_to_analyzer_button)
        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}