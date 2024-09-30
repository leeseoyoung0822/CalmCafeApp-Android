package com.example.calmcafeapp.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class SearchMapResponse(
    @SerialName("lastBuildDate")
    val lastBuildDate: String,
    @SerialName("total")
    val total: Int,
    @SerialName("start")
    val start: Int,
    @SerialName("display")
    val display: Int,
    @SerialName("items")
    val items: List<LocalItem>
)

data class LocalItem(
    @SerialName("title")
    val title: String,
    @SerialName("link")
    val link: String,
    @SerialName("category")
    val category: String,
    @SerialName("description")
    val description: String,
    @SerialName("telephone")
    val telephone: String,
    @SerialName("address")
    val address: String,
    @SerialName("roadAddress")
    val roadAddress: String,
    @SerialName("mapx")
    val mapx: Double,
    @SerialName("mapy")
    val mapy: Double
)