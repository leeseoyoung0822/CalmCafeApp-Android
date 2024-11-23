package com.example.calmcafeapp.data

import com.google.gson.annotations.SerializedName

data class M_CafeDetailResponse(
    @SerializedName("isSuccess")
    val isSuccess: Boolean,

    @SerializedName("code")
    val code: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("result")
    val result: M_CafeDetailResult
)

data class M_CafeDetailResult(
    @SerializedName("storeId")
    val storeId: Int,

    @SerializedName("storeName")
    val storeName: String,

    @SerializedName("storeCongestionLevel")
    val storeCongestionLevel: String,

    @SerializedName("userCongestionLevel")
    val userCongestionLevel: String,

    @SerializedName("address")
    val address: String,

    @SerializedName("image")
    val image: String,

    @SerializedName("openingTime")
    val openingTime: String,

    @SerializedName("closingTime")
    val closingTime: String,

    @SerializedName("maxCustomerCount")
    val maxCustomerCount: Int,

    @SerializedName("lastOrderTime")
    val lastOrderTime: String
)

data class TimeData(
    @SerializedName("hour")
    val hour: Int,

    @SerializedName("minute")
    val minute: Int,

    @SerializedName("second")
    val second: Int,

    @SerializedName("nano")
    val nano: Int
)
