package com.example.calmcafeapp.data

import com.google.gson.annotations.SerializedName

data class CongestionLevelResponse(

    @SerializedName("isSuccess")
    val isSuccess: Boolean = false,

    @SerializedName("code")
    val code: String = "",

    @SerializedName("message")
    val message: String = "",

    @SerializedName("result")
    val result: String? = null
)
