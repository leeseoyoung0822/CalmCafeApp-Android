package com.example.calmcafeapp.api

import com.example.calmcafeapp.data.FavoriteResponse
import com.example.calmcafeapp.data.PointCouponResponse
import com.example.calmcafeapp.data.SurveyRequest
import com.example.calmcafeapp.data.SurveyResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH

interface SettingService {
    @PATCH("/user/survey")
    fun submitSurvey(
        @Header("Authorization") accessToken: String,
        @Body request: SurveyRequest
    ): Call<SurveyResponse>

    @GET("/store/favorite/list")
    fun getFavoriteStores(
        @Header("Authorization") accessToken: String
    ): Call<FavoriteResponse>

    @GET("/point/list")
    fun getPointCoupons(
        @Header("Authorization") accessToken: String
    ): Call<PointCouponResponse>
}