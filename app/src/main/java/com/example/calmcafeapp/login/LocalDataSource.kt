package com.example.calmcafeapp.login

import android.content.Context
import android.content.SharedPreferences

object LocalDataSource {
    private lateinit var sharedPreferences: SharedPreferences

    fun init(context: Context) {
        sharedPreferences = context.getSharedPreferences("app_prefs", Context.MODE_PRIVATE)
    }

    fun setAccessToken(token: String) {
        sharedPreferences.edit().putString("access_token", token).apply()
    }

    fun getAccessToken() : String? {
        return sharedPreferences.getString("access_token","")
    }
    fun setRefreshToken(token: String) {
        sharedPreferences.edit().putString("refresh_token", token).apply()
    }

    fun getRefreshToken() : String? {
        return sharedPreferences.getString("refresh_token","")
    }

    fun setUserId(id: String) {
        sharedPreferences.edit().putString("user_id", id).apply()
    }

    fun getUserId() : String? {
        return sharedPreferences.getString("user_id","11")
    }
}