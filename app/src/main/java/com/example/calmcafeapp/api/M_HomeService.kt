package com.example.calmcafeapp.api

import com.example.calmcafeapp.data.CafeDetailResponse
import com.example.calmcafeapp.data.M_CafeDetailResponse
import com.example.calmcafeapp.data.StoreResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.Path
import retrofit2.http.Query

interface M_HomeService {
    @GET("/store/detail/cafe")
    fun getCafeDetail(
        @Header("Authorization") accessToken: String,
    ): Call<M_CafeDetailResponse>

    @PATCH("/store/modify/hours")
    fun modifyHours(
        @Header("Authorization") accessToken: String,
        @Query("openingTime") openingTime: String,
        @Query("closingTime") closingTime: String
    ): Call<StoreResponse>

    @PATCH("/store/modify/lastordertime")
    fun modifyLastOrderTime(
        @Header("Authorization") accessToken: String,
        @Query("lastOrderTime") lastOrderTime: String
    ): Call<StoreResponse>

    @PATCH("/store/modify/max-capacity")
    fun modifyMaxCapacity(
        @Header("Authorization") accessToken: String,
        @Query("maxCapacity") maxCapacity: Int
    ): Call<StoreResponse>
}