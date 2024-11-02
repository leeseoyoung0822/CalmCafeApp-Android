package com.example.calmcafeapp.data

data class CafeDetailResponse (
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: CafeDetail
)

data class CafeDetail(
    val id: Int,
    val name: String,
    val image: String,
    val distance: Double,
    val favoriteCount: Int,
    val isFavorite: Boolean,
    val storeCongestionLevel: String,
    val userCongestionLevel: String,
    val recommendStoreResDtoList: List<RecommendStore>,
    // 필요한 필드를 추가
)
data class MenuDetail(
    val id: Int,
    val name: String,
    val price: Int,
    val image: String?
)
data class RecommendStore(
    val id: Int,
    val name: String,
    val storeCongestionLevel: String,
    val address: String,
    val image: String?
)
data class PointMenuDetail(
    val id: Int,
    val name: String,
    val pointPrice: Int,
    val pointDiscount: Int,
    val image: String?
)

