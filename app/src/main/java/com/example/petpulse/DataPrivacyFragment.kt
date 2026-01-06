package com.example.petpulse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.petpulse.databinding.FragmentDataPrivacyBinding

class DataPrivacyFragment : Fragment() {

    private var _binding: FragmentDataPrivacyBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentDataPrivacyBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Set listeners for each switch to show a Toast message
        binding.switchAnalytics.setOnCheckedChangeListener { _, isChecked ->
            showToast("Analytics Data", isChecked)
        }

        binding.switchMarketing.setOnCheckedChangeListener { _, isChecked ->
            showToast("Marketing Communications", isChecked)
        }

        binding.switchPartners.setOnCheckedChangeListener { _, isChecked ->
            showToast("Third-Party Partners", isChecked)
        }
    }

    private fun showToast(settingName: String, isEnabled: Boolean) {
        val status = if (isEnabled) "enabled" else "disabled"
        Toast.makeText(requireContext(), "$settingName sharing $status", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
