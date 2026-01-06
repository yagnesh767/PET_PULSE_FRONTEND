package com.example.petpulse.model

data class AddPetRequest(
    val user_id: Int,
    val name: String,
    val species: String,
    val breed: String,
    val age: Int,
    val weight: Double,
    val gender: String
)
