package com.example.calmcafeapp.data

data class CafeData(
    val cafe_img : String,
    val cafe_name : String,
    val cafe_state: String,
    var isLiked: Boolean = false  // 기본값으로 좋아요 상태를 false로 설정
)
