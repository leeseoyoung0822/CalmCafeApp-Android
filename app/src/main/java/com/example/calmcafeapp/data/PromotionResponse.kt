package com.example.calmcafeapp.data

data class PromotionResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: List<Promotion>
)

data class Promotion(
    val id: Int,
    val startTime: String,
    val endTime: String,
    val discount: Int,
    val promotionType: String
)


