package com.example.petpulse.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.petpulse.repository.PetRepository

class AddNewPetViewModelFactory(private val repository: PetRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(AddNewPetViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return AddNewPetViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}