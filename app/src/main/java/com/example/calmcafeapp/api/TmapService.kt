package com.example.calmcafeapp.api

import com.example.calmcafeapp.data.PedestrianRouteRequest
import com.example.calmcafeapp.data.TmapRouteResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.POST


interface TmapService {
    @Headers(
        "Accept: application/json",
        "Content-Type: application/json"
    )
    @POST("routes/pedestrian")
    fun getPedestrianRoute(
        @Header("appKey") appKey: String,
        @Body request: PedestrianRouteRequest
    ): Call<TmapRouteResponse>
}