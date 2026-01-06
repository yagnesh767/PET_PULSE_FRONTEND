package com.example.petpulse.model

data class MedicalRecord(
    val id: Int,
    val pet_id: Int,
    val record_type: String,
    val title: String,
    val record_date: String,
    val veterinarian: String,
    val description: String?,
    val severity: String?,
    val file_path: String?,
    val created_at: String
)
