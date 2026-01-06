package com.example.petpulse.ui.vaccines

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petpulse.AddVaccineActivity
import com.example.petpulse.R
import com.example.petpulse.adapter.VaccineAdapter
import com.example.petpulse.model.VaccineListResponse
import com.example.petpulse.network.ApiClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class VaccinesFragment : Fragment(R.layout.fragment_vaccines) {

    private lateinit var adapter: VaccineAdapter
    private lateinit var recyclerVaccines: RecyclerView
    private lateinit var btnAddVaccine: Button
    private var petId: Int = -1

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // Initialize Views
        recyclerVaccines = view.findViewById(R.id.recyclerVaccines)
        btnAddVaccine = view.findViewById(R.id.btnAddVaccine)

        // Initialize Adapter
        adapter = VaccineAdapter()
        recyclerVaccines.layoutManager = LinearLayoutManager(requireContext())
        recyclerVaccines.adapter = adapter

        // Get Pet ID
        val prefs = requireContext().getSharedPreferences("user_session", android.content.Context.MODE_PRIVATE)
        petId = prefs.getInt("selected_pet_id", -1)

        // Button Listener
        btnAddVaccine.setOnClickListener {
            val intent = Intent(requireContext(), AddVaccineActivity::class.java)
            intent.putExtra("pet_id", petId)
            startActivity(intent)
        }

        // Load Data
        if (petId != -1) {
            loadVaccines()
        }
    }

    override fun onResume() {
        super.onResume()
        if (petId != -1) {
            loadVaccines()
        }
    }

    private fun loadVaccines() {
        ApiClient.apiService
            .listVaccines(petId)
            .enqueue(object : Callback<VaccineListResponse> {

                override fun onResponse(
                    call: Call<VaccineListResponse>,
                    response: Response<VaccineListResponse>
                ) {
                    if (response.isSuccessful && response.body() != null) {
                        val vaccines = response.body()!!.vaccines
                        android.util.Log.d("DEBUG_VACCINE", "API Vaccine Count: ${vaccines.size}")
                        adapter.setData(vaccines)
                    } else {
                        android.util.Log.e("DEBUG_VACCINE", "API Error: ${response.message()}")
                        adapter.setData(emptyList())
                    }
                }

                override fun onFailure(call: Call<VaccineListResponse>, t: Throwable) {
                    android.util.Log.e("DEBUG_VACCINE", "API Failure: ${t.message}")
                }
            })
    }
}
