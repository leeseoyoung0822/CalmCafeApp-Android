package com.example.calmcafeapp.api
import com.example.calmcafeapp.data.CafeDetailResponse
import com.example.calmcafeapp.data.RankingResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface RankingService {
    @GET("/ranking/congestion")
    fun getRealTimeRanking(@Query("location") location: String): Call<RankingResponse>

    @GET("/ranking/total")
    fun getTotalRanking(@Query("location") location: String): Call<RankingResponse>

    @GET("/ranking/favorite")
    fun getFavoriteRanking(@Query("location") location: String): Call<RankingResponse>

    @GET("/store/detail/user")
    fun getCafeDetail(
        @Query("storeId") storeId: Int,
        @Query("userLatitude") userLatitude: Double,
        @Query("userLongitude") userLongitude: Double
    ): Call<CafeDetailResponse>
}