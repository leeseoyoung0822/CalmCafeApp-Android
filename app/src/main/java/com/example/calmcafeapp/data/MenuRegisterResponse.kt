package com.example.calmcafeapp.data

data class MenuRegisterResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: MenuRegisterResult?
)

data class MenuRegisterResult(
    val id: Int,
    val name: String,
    val image: String,
    val price: Int,
    val pointPrice: Int
)
