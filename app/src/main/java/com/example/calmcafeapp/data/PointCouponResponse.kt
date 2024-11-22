package com.example.calmcafeapp.data

import com.google.gson.annotations.SerializedName

data class PointCouponResponse(
    @SerializedName("isSuccess")
    val isSuccess: Boolean,
    @SerializedName("code")
    val code: String,
    @SerializedName("message")
    val message: String,
    @SerializedName("result")
    val result: PointCouponResult
)

data class PointCouponResult(
    @SerializedName("pointCouponResDtoList")
    val pointCouponResDtoList: List<PointCoupon>
)

data class PointCoupon(
    @SerializedName("id")
    val id: Int,
    @SerializedName("storeName")
    val storeName: String,
    @SerializedName("menuName")
    val menuName: String,
    @SerializedName("expirationStart")
    val expirationStart: String,
    @SerializedName("expirationEnd")
    val expirationEnd: String,
    @SerializedName("discount")
    val discount: Int
)

