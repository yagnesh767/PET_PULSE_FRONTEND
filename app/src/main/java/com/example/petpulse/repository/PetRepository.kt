package com.example.petpulse.repository

import com.example.petpulse.network.ApiService
import okhttp3.MultipartBody
import okhttp3.RequestBody

class PetRepository(private val api: ApiService) {

    suspend fun addPet(
        userId: RequestBody,
        petName: RequestBody,
        species: RequestBody,
        breed: RequestBody,
        gender: RequestBody,
        age: RequestBody,
        weight: RequestBody,
        image: MultipartBody.Part?
    ) = api.addPet(
        userId,
        petName,
        species,
        breed,
        gender,
        age,
        weight,
        image
    )
}