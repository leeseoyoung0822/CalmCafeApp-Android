package com.example.calmcafeapp.data

import com.google.gson.annotations.JsonAdapter

@JsonAdapter(GeometryDeserializer::class)
sealed class Geometry {
    data class PointGeometry(
        val type: String,
        val coordinates: List<Double>
    ) : Geometry()

    data class LineStringGeometry(
        val type: String,
        val coordinates: List<List<Double>>
    ) : Geometry()
}