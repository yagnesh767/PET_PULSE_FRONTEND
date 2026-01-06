package com.example.petpulse

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petpulse.model.Pet
import com.example.petpulse.network.ApiClient
import com.example.petpulse.viewmodel.PetViewModel
import com.google.android.material.card.MaterialCardView
import kotlinx.coroutines.launch

class HomeFragment : Fragment() {

    private val petViewModel: PetViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        val view = inflater.inflate(R.layout.fragment_home, container, false)

        view.findViewById<ImageView>(R.id.notification_bell).setOnClickListener {
            startActivity(Intent(requireContext(), NotificationsPermissionsActivity::class.java))
        }

        view.findViewById<ImageView>(R.id.imgSelectedPet).setOnClickListener {
            com.example.petpulse.ui.switchpet.SwitchPetBottomSheet(
                petViewModel.petsLiveData.value ?: emptyList()
            ) {
                // Refresh UI after selection
                onResume()
            }.show(
                parentFragmentManager,
                "PetSwitch"
            )
        }



        view.findViewById<TextView>(R.id.view_stores_on_map_button).setOnClickListener {
            val uri = Uri.parse("geo:0,0?q=pet+store+near+me")
            val intent = Intent(Intent.ACTION_VIEW, uri)
            intent.setPackage("com.google.android.apps.maps")
            if (intent.resolveActivity(requireActivity().packageManager) != null) {
                startActivity(intent)
            } else {
                val webUri = Uri.parse("https://www.google.com/maps/search/?api=1&query=pet+store+near+me")
                startActivity(Intent(Intent.ACTION_VIEW, webUri))
            }
        }

        view.findViewById<TextView>(R.id.view_details_button).setOnClickListener { 
            val prefs = requireContext().getSharedPreferences("user_session", Context.MODE_PRIVATE)
            val selectedPetId = prefs.getInt("selected_pet_id", -1)
            
            if (selectedPetId != -1) {
                val intent = Intent(requireContext(), PetDetailsActivity::class.java)
                intent.putExtra("pet_id", selectedPetId)
                startActivity(intent)
            } else {
                Toast.makeText(requireContext(), "No pet selected", Toast.LENGTH_SHORT).show()
            }
        }

        view.findViewById<Button>(R.id.ai_checkup_button).setOnClickListener {
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, AiVetChatFragment()) // Corrected class name
                .addToBackStack(null)
                .commit()
        }

        view.findViewById<MaterialCardView>(R.id.add_record_card).setOnClickListener {
             com.example.petpulse.utils.SelectedPetManager.pet?.let { pet ->
                val intent = Intent(requireContext(), AddMedicalRecordActivity::class.java)
                intent.putExtra("pet_id", pet.id)
                startActivity(intent)
             } ?: run {
                Toast.makeText(requireContext(), "No pet selected", Toast.LENGTH_SHORT).show()
             }
        }

        view.findViewById<MaterialCardView>(R.id.log_vaccine_card).setOnClickListener {
             com.example.petpulse.utils.SelectedPetManager.pet?.let { pet ->
                val intent = Intent(requireContext(), AddVaccineActivity::class.java)
                intent.putExtra("pet_id", pet.id)
                startActivity(intent)
             } ?: run {
                Toast.makeText(requireContext(), "No pet selected", Toast.LENGTH_SHORT).show()
             }
        }

        view.findViewById<MaterialCardView>(R.id.track_weight_card).setOnClickListener {
             com.example.petpulse.utils.SelectedPetManager.pet?.let { pet ->
                val intent = Intent(requireContext(), PetDetailsActivity::class.java)
                intent.putExtra("pet_id", pet.id)
                intent.putExtra("SELECTED_TAB", 3)
                startActivity(intent)
             } ?: run {
                Toast.makeText(requireContext(), "No pet selected", Toast.LENGTH_SHORT).show()
             }
        }

        view.findViewById<TextView>(R.id.see_all_vets).setOnClickListener {
            startActivity(Intent(requireContext(), FindAVetActivity::class.java))
        }

        view.findViewById<MaterialCardView>(R.id.vet_card_1).setOnClickListener {
            startActivity(Intent(requireContext(), ClinicDetailsActivity::class.java))
        }

        view.findViewById<Button>(R.id.btn_book_1).setOnClickListener {
            startActivity(Intent(requireContext(), BookAppointmentActivity::class.java))
        }

        return view
    }

    override fun onResume() {
        super.onResume()
        com.example.petpulse.utils.SelectedPetManager.pet?.let {
            val tvPetBreedAge = view?.findViewById<TextView>(R.id.tvPetBreedAge)
            val tvPetStatus = view?.findViewById<TextView>(R.id.tvPetStatus)
            val tvPetWeight = view?.findViewById<TextView>(R.id.tvPetWeight)

            tvPetName?.text = it.pet_name
            
            // Format Breed and Age
            val breed = it.breed ?: "Unknown Breed"
            val age = calculateAge(it.birth_date)
            tvPetBreedAge?.text = "$breed • $age"

            // Weight
            val weightVal = it.weight?.toString() ?: "-"
            tvPetWeight?.text = "$weightVal kg"

            // Status (Mock for now, or could come from backend)
            tvPetStatus?.text = "Active"

            if (imgPet != null) {
                com.bumptech.glide.Glide.with(this)
                    .load(com.example.petpulse.network.ApiClient.getFullImageUrl(it.image_url))
                    .placeholder(R.drawable.ic_pet_placeholder)
                    .error(R.drawable.ic_pet_placeholder)
                    .into(imgPet)
            }
            if (petAvatar != null) {
                com.bumptech.glide.Glide.with(this)
                    .load(com.example.petpulse.network.ApiClient.getFullImageUrl(it.image_url))
                    .placeholder(R.drawable.ic_pet_placeholder)
                    .error(R.drawable.ic_pet_placeholder)
                    .into(petAvatar)
            }
        }
    }

    private fun calculateAge(birthDateStr: String?): String {
        if (birthDateStr.isNullOrEmpty()) return "Unknown Age"
        return try {
            val sdf = java.text.SimpleDateFormat("yyyy-MM-dd", java.util.Locale.US)
            val birthDate = sdf.parse(birthDateStr)
            val today = java.util.Calendar.getInstance()
            val dob = java.util.Calendar.getInstance().apply { time = birthDate!! }
            
            var age = today.get(java.util.Calendar.YEAR) - dob.get(java.util.Calendar.YEAR)
            if (today.get(java.util.Calendar.DAY_OF_YEAR) < dob.get(java.util.Calendar.DAY_OF_YEAR)) {
                age--
            }
            if (age == 0) {
                 // Calculate months
                 var months = today.get(java.util.Calendar.MONTH) - dob.get(java.util.Calendar.MONTH)
                 if (months < 0) months += 12
                 return "$months months"
            }
            "$age years"
        } catch (e: Exception) {
            "Unknown Age"
        }
        }
    }
}
