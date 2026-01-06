package com.example.petpulse.ui.pet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petpulse.AddMedicalRecordActivity
import com.example.petpulse.R
import com.example.petpulse.adapter.MedicalRecordAdapter
import com.example.petpulse.model.TimelineResponse
import com.example.petpulse.network.ApiClient
import com.example.petpulse.network.ApiService
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MedicalFragment : Fragment() {

    private lateinit var adapter: MedicalRecordAdapter
    private lateinit var recyclerMedical: RecyclerView
    private var petId: Int = -1

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_medical, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val prefs = requireContext().getSharedPreferences("user_session", android.content.Context.MODE_PRIVATE)
        petId = prefs.getInt("selected_pet_id", -1)

        recyclerMedical = view.findViewById(R.id.recyclerMedical)
        adapter = MedicalRecordAdapter()
        recyclerMedical.layoutManager = LinearLayoutManager(requireContext())
        recyclerMedical.adapter = adapter

        view.findViewById<TextView>(R.id.add_record_button).setOnClickListener {
            val intent = Intent(requireActivity(), AddMedicalRecordActivity::class.java)
            intent.putExtra("pet_id", petId)
            startActivity(intent)
        }

        if (petId != -1) {
            loadMedicalRecords()
        }
    }

    override fun onResume() {
        super.onResume()
        if (petId != -1) {
            loadMedicalRecords() // Refresh on return
        }
    }

    private fun loadMedicalRecords() {
        // Updated to use getPetTimeline (which maps to medical records)
        ApiClient.apiService
            .getPetTimeline(petId)
            .enqueue(object : Callback<TimelineResponse> {

                override fun onResponse(
                    call: Call<TimelineResponse>,
                    response: Response<TimelineResponse>
                ) {
                    val allRecords = response.body()?.medical_records
                    android.util.Log.d("DEBUG_MEDICAL", "Raw API Count: ${allRecords?.size}")

                    val medicalOnly = allRecords?.filter { it.record_type == "Medical" }
                    android.util.Log.d("DEBUG_MEDICAL", "Filtered Medical Count: ${medicalOnly?.size}")

                    adapter.setRecords(medicalOnly ?: emptyList())
                    android.util.Log.d("DEBUG_MEDICAL", "Adapter Item Count: ${adapter.itemCount}")
                }

                override fun onFailure(call: Call<TimelineResponse>, t: Throwable) {
                    android.util.Log.e("DEBUG_MEDICAL", "API Failure: ${t.message}")
                }
            })
    }
}
