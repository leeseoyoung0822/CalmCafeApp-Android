package com.example.calmcafeapp.data

data class TokenResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: TokenResult
)
data class TokenResult(
    val accessToken: String,
    val refreshToken: String,
    val signIn: String,
    val role: String
)

