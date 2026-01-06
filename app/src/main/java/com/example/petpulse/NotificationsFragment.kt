package com.example.petpulse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.petpulse.databinding.FragmentNotificationsBinding

class NotificationsFragment : Fragment() {

    private var _binding: FragmentNotificationsBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentNotificationsBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // Set listeners for each switch to show a Toast message
        binding.switchReminders.setOnCheckedChangeListener { _, isChecked ->
            showToast("Reminders", isChecked)
        }

        binding.switchHealthAlerts.setOnCheckedChangeListener { _, isChecked ->
            showToast("Health Alerts", isChecked)
        }

        binding.switchAppointments.setOnCheckedChangeListener { _, isChecked ->
            showToast("Appointments", isChecked)
        }

        binding.switchPromotions.setOnCheckedChangeListener { _, isChecked ->
            showToast("Promotions", isChecked)
        }
    }

    private fun showToast(settingName: String, isEnabled: Boolean) {
        val status = if (isEnabled) "enabled" else "disabled"
        Toast.makeText(requireContext(), "$settingName notifications $status", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
