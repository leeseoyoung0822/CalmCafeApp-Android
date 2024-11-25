package com.example.calmcafeapp.data


data class PurchaseResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Int
)


