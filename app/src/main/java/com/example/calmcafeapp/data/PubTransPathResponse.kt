package com.example.calmcafeapp.data

import com.google.gson.annotations.SerializedName

data class PubTransPathResponse(
    val result: PubTransPathResult
)

data class PubTransPathResult(
    val searchType: Int,
    val outTrafficCheck: Int,
    val busCount: Int,
    val subwayCount: Int,
    val subwayBusCount: Int,
    val pointDistance: Double,
    val path: List<Path>
)

data class Path(
    val pathType: Int,
    val info: Info,
    val subPath: List<SubPath>
)

data class Guide(
    val x: Double,
    val y: Double,
    val type: Int,
    val distance: Int,
    val duration: Int
)

data class Info(
    val totalTime: Int,
    val totalWalk: Int,
    val payment: Int,
    val busTransitCount: Int,
    val subwayTransitCount: Int,
    val mapObj: String?,
)

data class SubPath(
    @SerializedName("trafficType")
    val trafficType: Int,
    @SerializedName("distance")
    val distance: Double,
    @SerializedName("sectionTime")
    val sectionTime: Int,
    @SerializedName("stationCount")
    val stationCount: Int?,
    @SerializedName("lane")
    val lane: List<Lane>?,
    @SerializedName("startName")
    val startName: String?,
    @SerializedName("startX")
    val startX: Double?,
    @SerializedName("startY")
    val startY: Double?,
    @SerializedName("endName")
    val endName: String?,
    @SerializedName("endX")
    val endX: Double?,
    @SerializedName("endY")
    val endY: Double?,
    @SerializedName("passShape")
    val passShape: String?,
    @SerializedName("guide")
    val guide: List<Guide>?,
    @SerializedName("passStopList")
    val passStopList: PassStopList?,
    )

data class Lane(
    @SerializedName("busNo")
    val busNo: String?,
    @SerializedName("type")
    val type: Int,
    @SerializedName("busID")
    val busID: Int?,
    @SerializedName("busLocalBlID")
    val busLocalBlID: String?,
    @SerializedName("busCityCode")
    val busCityCode: Int?,
    @SerializedName("busProviderCode")
    val busProviderCode: Int?,
    @SerializedName("name")
    val name: String?,
    @SerializedName("subwayCode")
    val subwayCode: Int?,
    @SerializedName("subwayCityCode")
    val subwayCityCode: Int?
)

data class PassStopList(
    @SerializedName("stations")
    val stations: List<Station>
)

data class Station(
    @SerializedName("index")
    val index: Int,
    @SerializedName("stationID")
    val stationID: Int,
)
