package com.example.calmcafeapp.api

import com.example.calmcafeapp.data.ReverseGeocodingResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NaverReverseGeocodingService {
    @GET("map-reversegeocode/v2/gc")
    fun reverseGeocode(
        @Query("coords") coords: String,
        @Query("orders") orders: String = "addr",
        @Query("output") output: String = "json"
    ): Call<ReverseGeocodingResponse>
}