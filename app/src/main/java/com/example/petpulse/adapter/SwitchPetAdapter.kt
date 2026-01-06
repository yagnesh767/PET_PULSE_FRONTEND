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

class SwitchPetAdapter(
    private val pets: List<Pet>,
    private val onClick: (Pet) -> Unit
) : RecyclerView.Adapter<SwitchPetAdapter.VH>() {

    inner class VH(view: View) : RecyclerView.ViewHolder(view) {
        val name: TextView = view.findViewById(R.id.tvPetName)
        val image: ImageView = view.findViewById(R.id.ivPetImage)
    }

    override fun onCreateViewHolder(p: ViewGroup, v: Int): VH {
        val view = LayoutInflater.from(p.context)
            .inflate(R.layout.item_switch_pet, p, false)
        return VH(view)
    }

    override fun onBindViewHolder(h: VH, i: Int) {
        val pet = pets[i]
        h.name.text = pet.pet_name
        h.itemView.setOnClickListener { onClick(pet) }

        val imageUrl = ApiClient.getFullImageUrl(pet.image_url)
        Glide.with(h.image.context)
            .load(imageUrl)
            .placeholder(R.drawable.ic_pet_placeholder)
            .into(h.image)
    }

    override fun getItemCount() = pets.size
}