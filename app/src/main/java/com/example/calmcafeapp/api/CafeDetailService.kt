package com.example.calmcafeapp.api

import com.example.calmcafeapp.data.CafeDetailResponse
import com.example.calmcafeapp.data.MapResponse
import com.example.calmcafeapp.data.PurchaseResponse
import com.example.calmcafeapp.data.SearchMapResponse
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.Call
import retrofit2.http.Header

interface CafeDetailService {
    @GET("/store/detail/user")
    fun getCafeInfo(
        @Header("Authorization") accessToken: String,
        @Query("storeId") storeId: Int,
        @Query("userLatitude") userLatitude: Double,
        @Query("userLongitude") userLongitude: Double): Call<CafeDetailResponse>

    @GET("/store/")
    fun fetchStore(
        @Query("userAddress") userAddress : String
    ): Call<MapResponse>

    @GET("/point/buy")
    fun purchaseItem(
        @Header("Authorization") authorization: String,
        @Query("menuId") menuId: Int
    ): Call<PurchaseResponse>
}