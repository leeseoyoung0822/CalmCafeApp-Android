package com.example.calmcafeapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.calmcafeapp.apiManager.ApiManager
import com.example.calmcafeapp.data.LocalItem
import com.example.calmcafeapp.data.ReverseGeocodingResponse
import com.example.calmcafeapp.data.SearchMapResponse
import com.naver.maps.geometry.LatLng
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val naverApiService = ApiManager.naverApiService
    private val reverseGeocodingService = ApiManager.naverReverseGeocodingService

    // 카페 목록을 담을 LiveData
    private val _cafes = MutableLiveData<List<LocalItem>>()
    val cafes: LiveData<List<LocalItem>> get() = _cafes

    // 에러 메시지를 담을 LiveData
    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    // 로딩 상태를 담을 LiveData
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // 주소를 담을 LiveData
    private val _address = MutableLiveData<String>()
    val address: LiveData<String> get() = _address

    // 좌표로부터 주소를 가져오는 함수
    fun getAddressFromCoordinates(latitude: Double, longitude: Double) {
        val coords = "$longitude,$latitude" // 경도,위도 순서로 문자열 생성

        val call = reverseGeocodingService.reverseGeocode(
            coords = coords,
            orders = "roadaddr,addr",
            output = "json"
        )

        call.enqueue(object : Callback<ReverseGeocodingResponse> {
            override fun onResponse(call: Call<ReverseGeocodingResponse>, response: Response<ReverseGeocodingResponse>) {
                if (response.isSuccessful) {
                    val results = response.body()?.results
                    if (!results.isNullOrEmpty()) {
                        Log.d("add1", "${response.body()}")
                        val region = results[0].region
                        val area1 = region.area1.name
                        val area2 = region.area2.name
                        val area3 = region.area3.name

                        val address = "$area1 $area2 $area3"
                        _address.value = address
                    } else {
                        Log.d("add", "${response.body()}")
                        _errorMessage.value = "주소 정보를 가져올 수 없습니다."
                    }
                } else {
                    _errorMessage.value = "Reverse Geocoding API 응답 오류"
                }
            }

            override fun onFailure(call: Call<ReverseGeocodingResponse>, t: Throwable) {
                _errorMessage.value = "Reverse Geocoding API 호출 실패"
            }
        })
    }

    // 주소에서 지역명을 추출하는 함수
    fun extractAreaFromAddress(address: String): String {
        val parts = address.split(" ")
        return if (parts.size >= 2) {
            "${parts[0]} ${parts[1]}"
        } else {
            address
        }
    }

    // 카페 검색 함수
    fun searchCafesInArea(area: String) {
        val query = "$area 카페"

        _isLoading.value = true

        val call = naverApiService.searchLocal(
            query = query,
            display = 20,
            start = 1,
            sort = "random"
        )

        call.enqueue(object : Callback<SearchMapResponse> {
            override fun onResponse(call: Call<SearchMapResponse>, response: Response<SearchMapResponse>) {
                Log.d("search","${response.body()}")
                _isLoading.value = false
                if (response.isSuccessful) {
                    val items = response.body()?.items ?: emptyList()
                    Log.d("item", "${items}")
                    // 여기서items가 null 값 반환
                    _cafes.value = items
                } else {
                    _errorMessage.value = "API 호출에 실패했습니다."
                }
            }

            override fun onFailure(call: Call<SearchMapResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "네트워크 오류가 발생했습니다."
            }
        })
    }
}