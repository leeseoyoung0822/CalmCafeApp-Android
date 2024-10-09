package com.example.calmcafeapp.api

import com.example.calmcafeapp.data.PubTransPathResponse
import com.example.calmcafeapp.data.RouteGraphicResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ODsayService {

    @GET("searchPubTransPathT")
    fun searchPubTransPath(
        @Query("SX") startX: Double,
        @Query("SY") startY: Double,
        @Query("EX") endX: Double,
        @Query("EY") endY: Double,
        @Query("OPT") opt: Int = 0,
        @Query("SearchType") searchType: Int = 0,
        @Query("SearchPathType") searchPathType: Int = 0,
        @Query("lang") lang: Int = 0,
        @Query("output") output: String = "json"
    ): Call<PubTransPathResponse>

    @GET("loadLane")
    fun getRouteGraphicData(
        @Query("mapObject") mapObject: String,
        @Query("lang") lang: Int = 0,
        @Query("output") output: String = "json"
    ): Call<RouteGraphicResponse>
}
