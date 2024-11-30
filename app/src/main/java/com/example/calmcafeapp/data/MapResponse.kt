package com.example.calmcafeapp.data

data class MapResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: Result
)

data class Result(
    val storePosDtoList: List<StorePosDto>
)

data class StorePosDto(
    val id: Int,
    val congestionLevel : String,
    val latitude: Double,
    val longitude: Double,
    val address: String,
    val name : String?=""
)
