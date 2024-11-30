package com.example.calmcafeapp.ui


import android.location.Location
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.NaverMap
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.example.calmcafeapp.data.StorePosDto
import com.example.calmcafeapp.R

class MarkerClusterer(
    private val naverMap: NaverMap,
    private val clusterDistance: Double // 클러스터링 거리 (단위: 지도 좌표)
) {
    private val markers = mutableListOf<Marker>()

    fun setMarkers(markerData: List<StorePosDto>) {
        clearMarkers()

        val clusters = clusterData(markerData)

        // 클러스터 생성
        for (cluster in clusters) {
            if (cluster.size > 1) {
                val center = calculateClusterCenter(cluster)
                val marker = Marker().apply {
                    position = center
                    captionText = "${cluster.size}개"
                    icon = OverlayImage.fromResource(R.drawable.marker_busy)
                    map = naverMap
                }
                markers.add(marker)
            } else {
                val item = cluster.first()
                val marker = Marker().apply {
                    position = LatLng(item.latitude, item.longitude)
                    icon = when (item.congestionLevel) {
                        "한산" -> OverlayImage.fromResource(R.drawable.marker_calm)
                        "보통" -> OverlayImage.fromResource(R.drawable.marker_normal)
                        "혼잡" -> OverlayImage.fromResource(R.drawable.marker_busy)
                        else -> OverlayImage.fromResource(R.drawable.marker_busy)
                    }
                    map = naverMap
                }
                markers.add(marker)
            }
        }
    }

    private fun clusterData(data: List<StorePosDto>): List<List<StorePosDto>> {
        val clusters = mutableListOf<MutableList<StorePosDto>>()

        for (item in data) {
            var addedToCluster = false
            for (cluster in clusters) {
                if (isWithinClusterDistance(cluster.first(), item)) {
                    cluster.add(item)
                    addedToCluster = true
                    break
                }
            }
            if (!addedToCluster) {
                clusters.add(mutableListOf(item))
            }
        }
        return clusters
    }

    private fun isWithinClusterDistance(a: StorePosDto, b: StorePosDto): Boolean {
        val distance = FloatArray(1)
        Location.distanceBetween(a.latitude, a.longitude, b.latitude, b.longitude, distance)
        return distance[0] <= clusterDistance
    }

    private fun calculateClusterCenter(cluster: List<StorePosDto>): LatLng {
        val latSum = cluster.sumOf { it.latitude }
        val lngSum = cluster.sumOf { it.longitude }
        return LatLng(latSum / cluster.size, lngSum / cluster.size)
    }

    private fun clearMarkers() {
        markers.forEach { it.map = null }
        markers.clear()
    }
}