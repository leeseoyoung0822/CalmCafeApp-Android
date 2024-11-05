package com.example.calmcafeapp.api

import com.example.calmcafeapp.data.CafeDetailResponse
import com.example.calmcafeapp.data.FavoriteResponse
import com.example.calmcafeapp.data.RankingResponse
import retrofit2.Call
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface RankingService {
    @GET("/ranking/congestion")
    fun getRealTimeRanking(@Header("Authorization") accessToken: String,
                           @Query("location") location: String): Call<RankingResponse>

    @GET("/ranking/total")
    fun getTotalRanking(@Header("Authorization") accessToken: String,@Query("location") location: String): Call<RankingResponse>

    @GET("/ranking/favorite")
    fun getFavoriteRanking(@Header("Authorization") accessToken: String,@Query("location") location: String): Call<RankingResponse>

    @GET("/store/detail/user")
    fun getCafeDetail(
        @Query("storeId") storeId: Int,
        @Query("userLatitude") userLatitude: Double,
        @Query("userLongitude") userLongitude: Double
    ): Call<CafeDetailResponse>

    @POST("/store/favorite/create/{store-id}")
    fun addFavorite(
        @Header("Authorization") accessToken: String,
        @Path("store-id") storeId: Int,
    ): Call<FavoriteResponse>

    @DELETE("/store/favorite/delete/{store-id}")
    fun removeFavorite(
        @Header("Authorization") accessToken: String,
        @Path("store-id") storeId: Int
    ): Call<FavoriteResponse>

}