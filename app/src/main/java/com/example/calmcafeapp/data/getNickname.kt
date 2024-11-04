package com.example.calmcafeapp.data


import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class getNickname(
    @SerialName("code")
    val code: String,
    @SerialName("isSuccess")
    val isSuccess: Boolean,
    @SerialName("message")
    val message: String,
    @SerialName("result")
    val result: Boolean
)

@Serializable
data class getNicknameRequest(
    @SerialName("nickname")
    val nickname: String
)