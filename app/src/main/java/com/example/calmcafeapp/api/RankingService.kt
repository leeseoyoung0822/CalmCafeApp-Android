package com.example.calmcafeapp.api

import com.example.calmcafeapp.data.CafeData
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RankingService {
    @GET("/cafes/rankings") // 실제 엔드포인트로 변경
    fun getCafeRankings(
        @Query("region") region: String,
        @Query("category") category: String
    ): Call<List<CafeData>>
}