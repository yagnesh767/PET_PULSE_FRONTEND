package com.example.petpulse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.petpulse.databinding.FragmentMonthlySummaryBinding

class MonthlySummaryFragment : Fragment() {

    private var _binding: FragmentMonthlySummaryBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentMonthlySummaryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Back arrow navigation -> Returns to AI Hub
        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Primary button -> Navigates to Health Report screen
        binding.viewDetailedReportButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, HealthReportFragment()) // Assumes HealthReportFragment exists
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
