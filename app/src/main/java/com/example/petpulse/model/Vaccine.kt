package com.example.petpulse.model

data class Vaccine(
    val id: Int,
    val pet_id: Int,
    val vaccine_name: String,
    val date_administered: String,
    val next_due_date: String,
    val vet_name: String?,
    val status: String?,
    val file_path: String?,
    val record_type: String = "Vaccine"
)
