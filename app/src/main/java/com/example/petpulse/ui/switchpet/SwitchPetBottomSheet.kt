package com.example.petpulse.ui.switchpet

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.petpulse.AddNewPetActivity
import com.example.petpulse.R
import com.example.petpulse.adapter.SwitchPetAdapter
import com.example.petpulse.model.Pet
import com.example.petpulse.utils.ActivePetManager
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

class SwitchPetBottomSheet(
    private val pets: List<Pet>,
    private val onSelected: () -> Unit
) : BottomSheetDialogFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val v = inflater.inflate(R.layout.bottom_sheet_switch_pet, container, false)

        val rv = v.findViewById<RecyclerView>(R.id.rvPets)
        rv.layoutManager = LinearLayoutManager(context)
        rv.adapter = SwitchPetAdapter(pets) {
            ActivePetManager.activePetId = it.id
            ActivePetManager.activePetName = it.pet_name
            ActivePetManager.activePetImageUrl = it.image_url
            com.example.petpulse.utils.SelectedPetManager.pet = it
            onSelected()
            dismiss()
        }

        v.findViewById<Button>(R.id.btnAddPet).setOnClickListener {
            startActivity(Intent(requireContext(), AddNewPetActivity::class.java))
            dismiss()
        }

        return v
    }
}