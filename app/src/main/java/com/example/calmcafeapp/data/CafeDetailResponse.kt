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
    val openingTime: String,
    val closingTime: String,
    val lastOrderTime: String,
    val storeState: String,
    val storeCongestionLevel: String,
    val userCongestionLevel: String,
   /* val menuDetailResDtoList: List<MenuDetail>,
    val recommendStoreResDtoList: List<RecommendStore>,
    val pointMenuDetailResDtoList: List<PointMenuDetail>,
    val promotionDetailResDtoList: List<PromotionDetail>*/
    // 필요한 필드를 추가
)