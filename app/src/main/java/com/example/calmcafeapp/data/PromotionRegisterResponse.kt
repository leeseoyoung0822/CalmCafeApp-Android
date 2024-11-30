package com.example.calmcafeapp.data

data class PromotionRegisterResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: PromotionResult?
)

data class PromotionResult(
    val id: Int,
    val startTime: PromotionTime,
    val endTime: PromotionTime,
    val discount: Int,
    val promotionType: String, // Possible values: "TAKE_OUT", "IN_STORE"
    val promotionUsedState: String // Possible values: "USED", "NOT_USED"
)

data class PromotionTime(
    val hour: Int,
    val minute: Int,
    val second: Int,
    val nano: Int
)
