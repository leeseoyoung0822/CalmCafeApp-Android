package com.example.calmcafeapp.api

import com.example.calmcafeapp.data.MapResponse
import com.example.calmcafeapp.data.SearchMapResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call

interface MapService {
    @GET("v1/search/local.json")
    fun searchLocal(
        @Query("query") query: String,
        @Query("display") display: Int = 20,
        @Query("start") start: Int,
        @Query("sort") sort: String
    ): Call<SearchMapResponse>
}
