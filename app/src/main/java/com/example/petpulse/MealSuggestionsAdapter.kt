package com.example.petpulse

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class MealSuggestionsAdapter(
    private val mealSuggestions: List<MealSuggestion>,
    private val onBuyClicked: (MealSuggestion) -> Unit
) : RecyclerView.Adapter<MealSuggestionsAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_meal_suggestion, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mealSuggestion = mealSuggestions[position]
        holder.bind(mealSuggestion, onBuyClicked)
    }

    override fun getItemCount() = mealSuggestions.size

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val foodName: TextView = itemView.findViewById(R.id.food_name)
        private val foodBenefit: TextView = itemView.findViewById(R.id.food_benefit)
        private val petSuitability: TextView = itemView.findViewById(R.id.pet_suitability)
        private val buyButton: Button = itemView.findViewById(R.id.buy_button)

        fun bind(mealSuggestion: MealSuggestion, onBuyClicked: (MealSuggestion) -> Unit) {
            foodName.text = mealSuggestion.name
            foodBenefit.text = mealSuggestion.benefit
            petSuitability.text = mealSuggestion.suitability
            buyButton.setOnClickListener { onBuyClicked(mealSuggestion) }
        }
    }
}
