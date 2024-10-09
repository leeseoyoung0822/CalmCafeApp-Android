package com.example.calmcafeapp.data

data class PedestrianRouteRequest(
    val startX: Double,
    val startY: Double,
    val endX: Double,
    val endY: Double,
    val startName: String,
    val endName: String,
    val searchOption: String = "10", // 예시: 최단거리
    val sort: String = "custom"       // 예시: 라인노드, 포인트노드 순
)