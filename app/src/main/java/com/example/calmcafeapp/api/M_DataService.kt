package com.example.calmcafeapp.api

import com.example.calmcafeapp.data.CongestionDataResponse
import com.example.calmcafeapp.data.FavoriteDataResponse

import com.example.calmcafeapp.data.VisitDataResponse
import retrofit2.Call
import retrofit2.http.GET

interface M_DataService {
    @GET("/data-analysis/visit")
    fun getVisitData()
    : Call<VisitDataResponse>

    @GET("/data-analysis/favorite")
    fun getFavoriteData()
    : Call<FavoriteDataResponse>

    @GET("/data-analysis/congestion")
    fun getCongestionData()
    : Call<CongestionDataResponse>
}