package com.example.calmcafeapp.data

import com.google.gson.annotations.SerializedName

data class RankingResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: RankingResult
)

data class RankingResult(
    @SerializedName("storeRankingResDtoList") // 서버 필드명과 맞춰줌
    val storeRankingResDtoList: List<StoreRanking>
)

data class StoreRanking(
    val id: Int,
    val name: String?,
    val storeCongestionLevel: String?,
    val userCongestionLevel: String?,
    var isFavorite: Boolean,
    val image: String?,
    val address: String?,
    val latitude: Double, // 추가된 필드
    val longitude: Double  // 추가된 필드
)