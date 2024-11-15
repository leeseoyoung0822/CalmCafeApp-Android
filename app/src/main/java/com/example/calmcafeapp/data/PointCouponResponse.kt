package com.example.calmcafeapp.data

data class PointCouponResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: PointCouponResult
)
data class PointCouponResult(
    val pointCouponResDtoList: List<PointCoupon>
)

data class PointCoupon(
    val id: Int,
    val storeName: String,
    val menuName: String,
    val expirationStart: String,
    val expirationEnd: String,
    val discount: Int
)

