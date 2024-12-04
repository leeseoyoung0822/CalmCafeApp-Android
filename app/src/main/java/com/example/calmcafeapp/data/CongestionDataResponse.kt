package com.example.calmcafeapp.data

data class CongestionDataResponse(
    val isSuccess: Boolean,
    val code: String,
    val message: String,
    val result: CongestionDataResult
)

data class CongestionDataResult(
    val averageCongestionImageUrl: String,
    val busiestAndLeastBusyImageUrl: String
)