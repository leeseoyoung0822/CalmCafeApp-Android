package com.example.calmcafeapp.data

data class RegisterMenuResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: MenuResult?
)

data class MenuResult(
    val id: Long,
    val name: String,
    val pointPrice: Int,
    val pointDiscount: Int,
    val image: String
)
