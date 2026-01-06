package com.example.petpulse.model

data class VaccineListResponse(
    val status: String,
    val vaccines: List<Vaccine>
)
