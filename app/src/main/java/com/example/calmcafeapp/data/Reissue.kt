package com.example.calmcafeapp.data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Reissue(
    @SerialName("code")
    val code: String,
    @SerialName("isSuccess")
    val isSuccess: Boolean,
    @SerialName("message")
    val message: String,
    @SerialName("result")
    val result: ReissueResult
)

@Serializable
data class ReissueResult(
    @SerialName("accessToken")
    val accessToken: String,
    @SerialName("refreshToken")
    val refreshToken: String,
    @SerialName("signIn")
    val signIn: String
)