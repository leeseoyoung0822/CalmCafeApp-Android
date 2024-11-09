package com.example.calmcafeapp.data

data class FavoriteResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: FavoriteResult
)

data class FavoriteResult(
    val favoriteStoreDetailResDtoList: List<FavoriteStore>
)

data class FavoriteStore(
    val id: Int,
    val address: String,
    val storeCongestionLevel: String,
    val userCongestionLevel: String
)