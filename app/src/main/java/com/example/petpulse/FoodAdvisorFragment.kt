package com.example.petpulse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import androidx.fragment.app.Fragment

class FoodAdvisorFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_food_advisor, container, false)

        val backButton = view.findViewById<ImageView>(R.id.back_arrow)
        backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val mealSuggestionsButton = view.findViewById<Button>(R.id.view_meal_suggestions_button)
        mealSuggestionsButton.setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, RecommendedMealsFragment())
                .addToBackStack(null)
                .commit()
        }

        return view
    }
}