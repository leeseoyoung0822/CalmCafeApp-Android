package com.example.calmcafeapp.data

data class UserInfo(
    val email: String,
    val username: String,
    val provider: String = "kakao"
)
