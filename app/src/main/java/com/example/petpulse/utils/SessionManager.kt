package com.example.petpulse.utils

import android.content.Context
import android.content.SharedPreferences

class SessionManager(context: Context) {
    private val prefs: SharedPreferences = context.getSharedPreferences("user_session", Context.MODE_PRIVATE)

    fun getUserId(): Int {
        return prefs.getInt("user_id", -1)
    }
}
