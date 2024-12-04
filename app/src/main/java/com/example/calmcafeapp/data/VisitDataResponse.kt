package com.example.calmcafeapp.data

data class VisitDataResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: VisitDataResult
)

data class VisitDataResult(
    val ageImageUrl: String,
    val genderImageUrl: String,
    val favoriteMenuDistributionImageUrl : String
)
