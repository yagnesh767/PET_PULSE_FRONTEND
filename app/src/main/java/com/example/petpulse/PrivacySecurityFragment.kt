package com.example.petpulse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.petpulse.databinding.FragmentPrivacySecurityBinding

class PrivacySecurityFragment : Fragment() {

    private var _binding: FragmentPrivacySecurityBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentPrivacySecurityBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        binding.changePasswordCard.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, ChangePasswordFragment()) // Assumes ChangePasswordFragment exists
                .addToBackStack(null)
                .commit()
        }

        binding.dataPrivacyCard.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, DataPrivacyFragment()) // Assumes DataPrivacyFragment exists
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
