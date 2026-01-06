package com.example.petpulse.model

data class PetListResponse(
    val status: String,
    val pets: List<Pet>
)
