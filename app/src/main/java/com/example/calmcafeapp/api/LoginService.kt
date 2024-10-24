package com.example.calmcafeapp.api


import com.example.calmcafeapp.data.TokenResponse
import com.example.calmcafeapp.data.UserInfo
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface LoginService {
    @POST("/token/generate")
    fun generateToken(@Body userInfo: UserInfo): Call<TokenResponse>
}