package com.example.petpulse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.petpulse.databinding.FragmentGroomingCompletedBinding

class GroomingCompletedFragment : Fragment() {

    private var _binding: FragmentGroomingCompletedBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroomingCompletedBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Back arrow navigation -> Returns to Grooming Checklist
        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Primary button -> Returns to Grooming Assistant
        binding.backToAssistantButton.setOnClickListener {
            // Pop twice: GroomingCompleted -> GroomingChecklist -> GroomingAssistant
            parentFragmentManager.popBackStack()
            parentFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
