package com.example.petpulse.ui.pet

import com.example.petpulse.R
import com.example.petpulse.AddWeightLogActivity

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment

class WeightFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_weight, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        view.findViewById<TextView>(R.id.log_weight_button).setOnClickListener {
            val intent = Intent(requireActivity(), AddWeightLogActivity::class.java)
            startActivity(intent)
        }
    }
}
