package com.example.calmcafeapp.data

data class PointMenuRemovalResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: PointMenuDetails
)

data class PointMenuDetails(
    val id: Long,
    val name: String,
    val pointPrice: Int,
    val pointDiscount: Int,
    val image: String
)

