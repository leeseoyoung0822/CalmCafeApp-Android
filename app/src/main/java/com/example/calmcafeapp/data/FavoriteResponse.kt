package com.example.calmcafeapp.data

import com.google.gson.annotations.SerializedName

data class FavoriteResponse(
    @SerializedName("isSuccess")
    val isSuccess: Boolean,

    @SerializedName("code")
    val code: String,

    @SerializedName("message")
    val message: String,

    @SerializedName("result")
    val result: FavoriteResult
)

data class FavoriteResult(
    @SerializedName("favoriteStoreDetailResDtoList")
    val favoriteStoreDetailResDtoList: List<FavoriteStore>
)

data class FavoriteStore(
    @SerializedName("id")
    val id: Int,

    @SerializedName("name")
    val name: String,

    @SerializedName("isFavorite")
    val isFavorite: Boolean,

    @SerializedName("image")
    val image: String,

    @SerializedName("storeCongestionLevel")
    val storeCongestionLevel: String,

    @SerializedName("userCongestionLevel")
    val userCongestionLevel: String
)
