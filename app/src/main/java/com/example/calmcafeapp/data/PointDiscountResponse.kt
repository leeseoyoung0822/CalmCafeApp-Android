package com.example.calmcafeapp.data

data class PointDiscountResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: List<PointDiscount>
)

data class PointDiscount(
    val id : Int,
    val name: String,
    var pointDiscount: Int
)
