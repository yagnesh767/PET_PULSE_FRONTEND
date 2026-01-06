package com.example.petpulse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.Fragment
import com.example.petpulse.AiVetChatFragment
import com.example.petpulse.FindAVetFragment

class SymptomAnalysisFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_symptom_analysis, container, false)

        val backButton = view.findViewById<ImageView>(R.id.back_button)
        val summaryTextView = view.findViewById<TextView>(R.id.summary_text)
        val concernTextView = view.findViewById<TextView>(R.id.concern_text)
        val chatButton = view.findViewById<Button>(R.id.chat_button)
        val findVetButton = view.findViewById<Button>(R.id.find_vet_button)

        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        chatButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AiVetChatFragment())
                .addToBackStack(null)
                .commit()
        }

        findVetButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FindAVetFragment())
                .addToBackStack(null)
                .commit()
        }

        val q1Answer = arguments?.getInt("q1_answer", -1) ?: -1
        val q2Answer = arguments?.getInt("q2_answer", -1) ?: -1
        val q3Answer = arguments?.getInt("q3_answer", -1) ?: -1
        val q4Answer = arguments?.getInt("q4_answer", -1) ?: -1

        // Dummy analysis logic
        val concernLevel = if (q1Answer == R.id.option3 || q2Answer == R.id.option3 || q3Answer == R.id.option1 || q4Answer == R.id.option3) {
            "High"
        } else if (q1Answer == R.id.option2 || q2Answer == R.id.option2 || q4Answer == R.id.option2) {
            "Moderate"
        } else {
            "Low"
        }

        summaryTextView.text = "Q1: $q1Answer\nQ2: $q2Answer\nQ3: $q3Answer\nQ4: $q4Answer"
        concernTextView.text = "Concern Level: $concernLevel"

        return view
    }
}