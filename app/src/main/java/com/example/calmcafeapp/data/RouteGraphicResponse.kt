package com.example.calmcafeapp.data
import com.google.gson.annotations.SerializedName


data class RouteGraphicResponse(
    @SerializedName("result")
    val result: RouteGraphicResult
)

data class RouteGraphicResult(
    @SerializedName("lane")
    val lane: List<RouteLane>,
    @SerializedName("boundary")
    val boundary: Boundary
)

data class RouteLane(
    @SerializedName("class")
    val classType: Int,
    @SerializedName("type")
    val type: Int,
    @SerializedName("section")
    val section: List<RouteSection>
)

data class RouteSection(
    @SerializedName("graphPos")
    val graphPos: List<GraphPos>
)

data class GraphPos(
    @SerializedName("x")
    val x: Double,
    @SerializedName("y")
    val y: Double
)

data class Boundary(
    @SerializedName("left")
    val left: Double,
    @SerializedName("top")
    val top: Double,
    @SerializedName("right")
    val right: Double,
    @SerializedName("bottom")
    val bottom: Double
)