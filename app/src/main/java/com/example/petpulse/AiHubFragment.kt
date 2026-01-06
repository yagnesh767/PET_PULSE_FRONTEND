package com.example.petpulse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.petpulse.databinding.FragmentAiHubBinding

class AiHubFragment : Fragment() {

    private var _binding: FragmentAiHubBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAiHubBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.healthReportCard.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HealthReportFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.symptomCheckerCard.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, SymptomCheckerQ1Fragment())
                .addToBackStack(null)
                .commit()
        }

        binding.behaviorAnalyzerCard.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, BehaviorAnalyzerFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.foodAdvisorCard.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, FoodAdvisorFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.diseaseRiskCard.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, DiseaseRiskPredictorFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.aiVetChatCard.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AiVetChatFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.groomingAssistantCard.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, GroomingAssistantFragment())
                .addToBackStack(null)
                .commit()
        }

        binding.monthlySummaryCard.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, MonthlySummaryFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}