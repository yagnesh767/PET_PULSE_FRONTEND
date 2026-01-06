package com.example.petpulse

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment

class RecommendedMealsFragment : Fragment(R.layout.fragment_recommended_meals) {

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val buyButton1 = view.findViewById<Button>(R.id.buy_button_1)
        buyButton1.setOnClickListener {
            navigateToStoreActivity()
        }

        val buyButton2 = view.findViewById<Button>(R.id.buy_button_2)
        buyButton2.setOnClickListener {
            navigateToStoreActivity()
        }
    }

    private fun navigateToStoreActivity() {
        val intent = Intent(requireContext(), StoreActivity::class.java)
        startActivity(intent)
    }
}