package com.example.calmcafeapp.data

import android.util.Log
import com.naver.maps.geometry.LatLng
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
@Serializable
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
    val mapx: String,
    @SerialName("mapy")
    val mapy: String
) {
    // 위도와 경도를 계산하는 프로퍼티 추가
    val latLng: LatLng
        get() {
            // mapx와 mapy를 Double로 변환하고 1억으로 나눔
            val longitude = mapx.toDoubleOrNull()?.div(1e7) ?: 0.0
            val latitude = mapy.toDoubleOrNull()?.div(1e7) ?: 0.0

            // 변환된 좌표 로그로 확인
            Log.d("COORDINATES", "Converted latLng: LatLng{latitude=$latitude, longitude=$longitude}")

            return LatLng(latitude, longitude)
        }
}