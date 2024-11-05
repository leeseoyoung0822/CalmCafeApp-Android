package com.example.calmcafeapp.api

import com.example.calmcafeapp.data.SurveyRequest
import com.example.calmcafeapp.data.SurveyResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.Header
import retrofit2.http.PATCH

interface SurveyService {
    @PATCH("/user/survey")
    fun submitSurvey(
        @Header("Authorization") accessToken: String,
        @Body request: SurveyRequest
    ): Call<SurveyResponse>
}