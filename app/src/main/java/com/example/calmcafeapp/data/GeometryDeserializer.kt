package com.example.calmcafeapp.data

import com.google.gson.*
import java.lang.reflect.Type

class GeometryDeserializer : JsonDeserializer<Geometry> {
    override fun deserialize(json: JsonElement, typeOfT: Type, context: JsonDeserializationContext): Geometry {
        val jsonObject = json.asJsonObject
        val type = jsonObject.get("type").asString
        return when (type) {
            "Point" -> context.deserialize(json, Geometry.PointGeometry::class.java)
            "LineString" -> context.deserialize(json, Geometry.LineStringGeometry::class.java)
            else -> throw JsonParseException("Unsupported geometry type: $type")
        }
    }
}