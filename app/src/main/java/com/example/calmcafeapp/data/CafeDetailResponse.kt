// 파일명: CafeDetailResponse.kt
package com.example.calmcafeapp.data

import com.google.gson.annotations.SerializedName

data class CafeDetailResponse(
    @SerializedName("isSuccess")
    val isSuccess: Boolean = false,

    @SerializedName("code")
    val code: String = "",

    @SerializedName("message")
    val message: String = "",

    @SerializedName("result")
    val result: CafeDetailResult? = null
)

data class CafeDetailResult(
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("name")
    val name: String = "",

    @SerializedName("image")
    val image: String? = null,

    @SerializedName("distance")
    val distance: Int? = null,

    @SerializedName("point")
    val point: Int? = null,

    @SerializedName("favoriteCount")
    val favoriteCount: Int? = null,

    @SerializedName("isFavorite")
    val isFavorite: Boolean? = null,

    @SerializedName("openingTime")
    val openingTime: String? = null,

    @SerializedName("closingTime")
    val closingTime: String? = null,

    @SerializedName("lastOrderTime")
    val lastOrderTime: String? = null,

    @SerializedName("storeState")
    val storeState: String = "",

    @SerializedName("storeCongestionLevel")
    val storeCongestionLevel: String = "",

    @SerializedName("userCongestionLevel")
    val userCongestionLevel: String = "",

    @SerializedName("menuDetailResDtoList")
    val menuDetailResDtoList: List<MenuDetailResDto>? = null,

    @SerializedName("recommendStoreResDtoList")
    val recommendStoreResDtoList: List<RecommendStoreResDto>? = null,

    @SerializedName("pointMenuDetailResDtoList")
    val pointMenuDetailResDtoList: List<PointMenuDetailResDto>? = null,

    @SerializedName("promotionDetailResDtoList")
    val promotionDetailResDtoList: List<PromotionDetailResDto>? = null
)


data class TimeDetail(
    @SerializedName("hour")
    val hour: Int = 0,

    @SerializedName("minute")
    val minute: Int = 0,

    @SerializedName("second")
    val second: Int = 0,

    @SerializedName("nano")
    val nano: Int = 0
)

data class MenuDetailResDto(
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("name")
    val name: String = "",

    @SerializedName("price")
    val price: Int = 0,

    @SerializedName("image")
    val image: String? = null
)

data class RecommendStoreResDto(
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("name")
    val name: String = "",

    @SerializedName("storeCongestionLevel")
    val storeCongestionLevel: String = "",

    @SerializedName("userCongestionLevel")
    val userCongestionLevel: String = "",

    @SerializedName("address")
    val address: String = "",

    @SerializedName("image")
    val image: String? = null
)

data class PointMenuDetailResDto(
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("name")
    val name: String = "",

    @SerializedName("pointPrice")
    val pointPrice: Int = 0,

    @SerializedName("pointDiscount")
    val pointDiscount: Int = 0,

    @SerializedName("image")
    val image: String? = null
)

data class PromotionDetailResDto(
    @SerializedName("id")
    val id: Int = 0,

    @SerializedName("startTime")
    val startTime: String? = null,

    @SerializedName("endTime")
    val endTime: String? = null,

    @SerializedName("discount")
    val discount: Int = 0,

    @SerializedName("promotionType")
    val promotionType: String = "",

    @SerializedName("promotionUsedState")
    val promotionUsedState: String = ""
)
