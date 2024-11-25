package com.example.calmcafeapp.api

import com.example.calmcafeapp.data.CongestionLevelResponse
import com.example.calmcafeapp.data.FavoriteResponse
import com.example.calmcafeapp.data.MapResponse
import retrofit2.Call
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface LocationService {
    @FormUrlEncoded
    @POST("/congestion/input/create")
    fun createCongestion(
        @Header("Authorization") authorization: String,
        @Field("storeId") storeId: Int,
        @Field("congestionValue") congestionValue: Int
    ): Call<CongestionLevelResponse>
}
