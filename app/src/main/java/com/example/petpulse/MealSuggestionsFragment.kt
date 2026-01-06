package com.example.petpulse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.widget.Toolbar
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomnavigation.BottomNavigationView

class MealSuggestionsFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_meal_suggestions, container, false)

        val toolbar = view.findViewById<Toolbar>(R.id.toolbar)
        toolbar.setNavigationOnClickListener {
            parentFragmentManager.popBackStack()
        }

        val recyclerView = view.findViewById<RecyclerView>(R.id.recommended_meals_recyclerview)
        recyclerView.layoutManager = LinearLayoutManager(context)

        val mealSuggestions = listOf(
            MealSuggestion("High-Protein Kibble", "High protein, easy digestion", "Suitable for all ages & weights"),
            MealSuggestion("Omega-3 Rich Salmon", "Supports skin and coat health", "Great for adult dogs"),
            MealSuggestion("Fiber-Rich Veggie Mix", "Aids in healthy digestion", "Perfect for senior dogs")
        )

        recyclerView.adapter = MealSuggestionsAdapter(mealSuggestions) { 
            val bottomNav = activity?.findViewById<BottomNavigationView>(R.id.bottom_navigation_bar)
            bottomNav?.selectedItemId = R.id.nav_store
        }

        return view
    }
}
