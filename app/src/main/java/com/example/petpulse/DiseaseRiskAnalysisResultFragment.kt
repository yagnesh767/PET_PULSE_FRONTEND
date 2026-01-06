package com.example.petpulse

import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment

class DiseaseRiskAnalysisResultFragment : Fragment(R.layout.fragment_disease_risk_analysis_result) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle back button click
        view.findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }
}