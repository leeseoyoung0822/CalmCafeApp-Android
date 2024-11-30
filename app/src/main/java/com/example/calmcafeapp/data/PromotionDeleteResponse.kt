package com.example.calmcafeapp.data

data class PromotionDeleteResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: String? // Optional result message
)
