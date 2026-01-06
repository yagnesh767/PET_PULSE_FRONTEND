package com.example.petpulse

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class OverviewFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_overview, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        val prefs = requireContext().getSharedPreferences("user_session", android.content.Context.MODE_PRIVATE)
        val petId = prefs.getInt("selected_pet_id", -1)

        if (petId != -1) {
            loadOverview(petId, view)
        }
    }

    private fun loadOverview(petId: Int, view: View) {
        val call = com.example.petpulse.network.ApiClient.apiService.getPetOverview(petId)
        call.enqueue(object : retrofit2.Callback<okhttp3.ResponseBody> {
            override fun onResponse(call: retrofit2.Call<okhttp3.ResponseBody>, response: retrofit2.Response<okhttp3.ResponseBody>) {
                if (response.isSuccessful && response.body() != null) {
                    try {
                        val jsonString = response.body()!!.string()
                        val json = org.json.JSONObject(jsonString)
                        
                        val weight = json.optString("weight", "N/A")
                        val healthScore = json.optString("health_score", "N/A")
                        
                        // Update UI on main thread (Retrofit callbacks are on main thread usually, but good to be safe if generic)
                        // Retrofit on Android calls onResponse on Main Thread automatically.
                        
                        val tvWeight = view.findViewById<android.widget.TextView>(R.id.tvWeight)
                        val tvHealthScore = view.findViewById<android.widget.TextView>(R.id.tvHealthScore)
                        
                        if(tvWeight != null) tvWeight.text = "$weight kg"
                        if(tvHealthScore != null) tvHealthScore.text = "$healthScore/100"

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            override fun onFailure(call: retrofit2.Call<okhttp3.ResponseBody>, t: Throwable) {
                // Handle failure
            }
        })
    }
}