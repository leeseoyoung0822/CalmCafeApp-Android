package com.example.calmcafeapp.data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class KakaoLogin(
    @SerialName("code")
    val code: String,
    @SerialName("isSuccess")
    val isSuccess: Boolean,
    @SerialName("message")
    val message: String,
    @SerialName("result")
    val result: KakaoLoginResult
)


@Serializable
data class KakaoLoginResult(
    val accessToken: String,
    val refreshToken: String,
    val signIn: String,
    val role: String
)



@Serializable
data class KakaoLoginRequest(
    @SerialName("email")
    val email: String,
    @SerialName("provider")
    val provider: String,
    @SerialName("username")
    val username: String
)