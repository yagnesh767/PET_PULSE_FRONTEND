package com.example.petpulse.utils

import com.example.petpulse.model.GeneralResponse
import retrofit2.Response

fun extractMessage(response: Response<GeneralResponse>): String {
    return response.body()?.message
        ?: response.errorBody()?.string()
        ?: "Unknown error"
}
