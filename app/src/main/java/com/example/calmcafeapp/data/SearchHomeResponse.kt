package com.example.calmcafeapp.data

data class SearchHomeResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: SearchResult?
)

data class SearchResult(
    val latitude: Double,
    val longitude: Double,
    val searchStoreResDtoList: List<SearchStoreResDto>
)

data class SearchStoreResDto(
    val name: String,
    val id: Int,
    val image: String,
    val address: String
)