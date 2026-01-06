package com.example.petpulse.model

data class Pet(
    val id: Int,
    val user_id: Int,
    val pet_name: String,
    val species: String,
    val breed: String?,
    val gender: String,
    val birth_date: String?,
    val weight: Double?,
    val image_url: String?
)
