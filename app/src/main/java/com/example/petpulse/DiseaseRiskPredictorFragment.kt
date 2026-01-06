package com.example.petpulse

import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment

class DiseaseRiskPredictorFragment : Fragment(R.layout.fragment_disease_risk_predictor) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Handle back button click
        view.findViewById<Toolbar>(R.id.toolbar).setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Handle analyze button click
        view.findViewById<Button>(R.id.analyze_risk_button).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, DiseaseRiskAnalysisResultFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}