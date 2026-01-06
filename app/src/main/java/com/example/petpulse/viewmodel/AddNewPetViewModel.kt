package com.example.petpulse.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petpulse.model.SimpleResponse
import com.example.petpulse.repository.PetRepository
import kotlinx.coroutines.launch
import okhttp3.MultipartBody
import okhttp3.RequestBody

class AddNewPetViewModel(
    private val repository: PetRepository
) : ViewModel() {

    val petName = MutableLiveData<String>("")
    val breed = MutableLiveData<String>("")
    val age = MutableLiveData<String>("")
    val weight = MutableLiveData<String>("")
    val gender = MutableLiveData<String>("Male")

    private val _addPetResult = MutableLiveData<SimpleResponse>()
    val addPetResult: LiveData<SimpleResponse> = _addPetResult

    fun addPet(
        userId: RequestBody,
        petName: RequestBody,
        species: RequestBody,
        breed: RequestBody,
        gender: RequestBody,
        age: RequestBody,
        weight: RequestBody,
        image: MultipartBody.Part?
    ) {
        viewModelScope.launch {
            try {
                val response = repository.addPet(
                    userId,
                    petName,
                    species,
                    breed,
                    gender,
                    age,
                    weight,
                    image
                )

                if (response.isSuccessful && response.body() != null) {
                    _addPetResult.postValue(response.body())
                } else {
                    _addPetResult.postValue(
                        SimpleResponse("error", "Failed to add pet")
                    )
                }

            } catch (e: Exception) {
                _addPetResult.postValue(
                    SimpleResponse("error", e.message ?: "Error")
                )
            }
        }
    }
}