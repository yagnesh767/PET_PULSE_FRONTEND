package com.example.petpulse.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.petpulse.model.Pet
import com.example.petpulse.network.ApiClient
import kotlinx.coroutines.launch

class PetViewModel : ViewModel() {
    val petsLiveData = MutableLiveData<List<Pet>>()
    val selectedPet = MutableLiveData<Pet>()

    fun loadPets(userId: Int) {
        viewModelScope.launch {
            val response = ApiClient.apiService.getPets(userId)
            if (response.status == "success") {
                petsLiveData.postValue(response.pets)
            }
        }
    }
}