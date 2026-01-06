package com.example.petpulse.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.petpulse.R
import com.example.petpulse.model.Pet
import com.example.petpulse.network.ApiClient

class PetSwitchAdapter(
    private val pets: List<Pet>,
    private val onSelect: (Pet) -> Unit
) : RecyclerView.Adapter<PetSwitchAdapter.VH>() {

    class VH(v: View) : RecyclerView.ViewHolder(v) {
        val name: TextView = v.findViewById(R.id.txtPetName)
        val img: ImageView = v.findViewById(R.id.imgPet)
    }

    override fun onCreateViewHolder(p: ViewGroup, t: Int): VH {
        val v = LayoutInflater.from(p.context)
            .inflate(R.layout.item_pet_switch, p, false)
        return VH(v)
    }

    override fun onBindViewHolder(h: VH, i: Int) {
        val pet = pets[i]
        h.name.text = pet.pet_name

        if (!pet.image_url.isNullOrEmpty()) {
            Glide.with(h.img.context)
                .load(ApiClient.getFullImageUrl(pet.image_url))
                .into(h.img)
        }

        h.itemView.setOnClickListener {
            onSelect(pet)
        }
    }

    override fun getItemCount() = pets.size
}
