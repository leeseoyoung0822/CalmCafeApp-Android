
package com.example.calmcafeapp.data


data class Feature(
    val type: String,
    val geometry: Geometry,
    val properties: Properties
)

data class TmapRouteResponse(
    val type: String,
    val features: List<Feature>
)

data class Properties(
    val index: Int?,
    val pointIndex: Int?,
    val name: String?,
    val description: String?,
    val direction: String?,
    val nearPoiName: String?,
    val nearPoiX: String?,
    val nearPoiY: String?,
    val intersectionName: String?,
    val facilityType: String?,
    val facilityName: String?,
    val turnType: Int?,
    val pointType: String?,
    val lineIndex: Int?,
    val distance: Int?,
    val time: Int?,
    val roadType: Int?,
    val categoryRoadType: Int?
)