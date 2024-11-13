package com.example.calmcafeapp.data

data class UserProfileResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: UserProfile
)
data class UserProfile(
    val nickname: String,   // 유저 닉네임
    val point: Int,        // 포인트 수
    val profileImage: String // 프로필 이미지 URL
)
