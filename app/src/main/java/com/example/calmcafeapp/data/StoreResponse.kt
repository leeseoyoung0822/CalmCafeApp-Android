package com.example.calmcafeapp.data

import com.google.gson.annotations.SerializedName

data class StoreResponse(
    @SerializedName("isSuccess")
    val isSuccess: Boolean,

    @SerializedName("code")
    val code: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("result")
    val result: StoreResult?
)

data class StoreResult(
    @SerializedName("storeId")
    val storeId: Int,

    @SerializedName("storeName")
    val storeName: String,

    @SerializedName("storeCongestionLevel")
    val storeCongestionLevel: String,

    @SerializedName("userCongestionLevel")
    val userCongestionLevel: String
)
