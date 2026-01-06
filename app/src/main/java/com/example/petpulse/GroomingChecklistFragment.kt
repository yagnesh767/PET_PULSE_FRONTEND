package com.example.petpulse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.petpulse.databinding.FragmentGroomingChecklistBinding

class GroomingChecklistFragment : Fragment() {

    private var _binding: FragmentGroomingChecklistBinding? = null
    private val binding get() = _binding!!

    private var completedTasks = 0
    private val totalTasks = 5

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentGroomingChecklistBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val checkboxes = listOf(
            binding.checkboxBrushing,
            binding.checkboxBathing,
            binding.checkboxTrimming,
            binding.checkboxEarCleaning,
            binding.checkboxNailTrimming
        )

        checkboxes.forEach { checkbox ->
            checkbox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    completedTasks++
                } else {
                    completedTasks--
                }
                updateProgress()
            }
        }

        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.markDoneButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, GroomingCompletedFragment())
                .addToBackStack(null)
                .commit()
        }
    }

    private fun updateProgress() {
        binding.progressText.text = "Completed: $completedTasks / $totalTasks tasks"
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
