package com.example.calmcafeapp.data

data class FavoriteDataResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: FavoriteDataResult
)

data class FavoriteDataResult(
    val ageDistributionImageUrl: String,
    val genderDistributionImageUrl: String
)
