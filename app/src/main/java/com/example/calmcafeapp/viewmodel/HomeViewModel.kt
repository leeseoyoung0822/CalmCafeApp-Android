package com.example.calmcafeapp.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.calmcafeapp.BuildConfig
import com.example.calmcafeapp.apiManager.ApiManager
import com.example.calmcafeapp.data.Geometry
import com.example.calmcafeapp.data.GraphPos
import com.example.calmcafeapp.data.LocalItem
import com.example.calmcafeapp.data.Path
import com.example.calmcafeapp.data.PedestrianRouteRequest
import com.example.calmcafeapp.data.PubTransPathResponse
import com.example.calmcafeapp.data.ReverseGeocodingResponse
import com.example.calmcafeapp.data.RouteGraphicResponse
import com.example.calmcafeapp.data.SearchMapResponse
import com.example.calmcafeapp.data.TmapRouteResponse
import com.naver.maps.geometry.LatLng
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeViewModel : ViewModel() {

    private val naverApiService = ApiManager.naverApiService
    private val reverseGeocodingService = ApiManager.naverReverseGeocodingService
    private val odsayService = ApiManager.odsayService
    private val tmapService = ApiManager.tmapService

    private val _pubTransPaths = MutableLiveData<List<Path>?>()
    val pubTransPaths: MutableLiveData<List<Path>?> get() = _pubTransPaths

    // 노선 그래픽 데이터를 담을 LiveData
    private val _routeGraphicData = MutableLiveData<List<GraphPos>?>()
    val routeGraphicData: MutableLiveData<List<GraphPos>?> get() = _routeGraphicData

    // 에러 메시지를 담을 LiveData
    private val _graphicErrorMessage = MutableLiveData<String>()
    val graphicErrorMessage: LiveData<String> get() = _graphicErrorMessage

    private var expectedGraphicDataCount = 0
    private var receivedGraphicDataCount = 0
    private val _allGraphPosList = MutableLiveData<List<GraphPos>?>()
    val allGraphPosList: MutableLiveData<List<GraphPos>?> get() = _allGraphPosList


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

    // 현재 위치에서 대중교통 이용지까지의 경로 데이터
    private val _walkingRouteCoordinatesFromStart = MutableLiveData<List<LatLng>?>()
    val walkingRouteCoordinatesFromStart: MutableLiveData<List<LatLng>?> get() = _walkingRouteCoordinatesFromStart

    // 대중교통 내리는 곳에서 카페까지의 경로 데이터
    private val _walkingRouteCoordinatesToDestination = MutableLiveData<List<LatLng>?>()
    val walkingRouteCoordinatesToDestination: MutableLiveData<List<LatLng>?> get() = _walkingRouteCoordinatesToDestination


    fun resetRouteData() {
        _pubTransPaths.value = null  // 대중교통 경로 초기화
        _walkingRouteCoordinatesFromStart.value = null  // 도보 경로 초기화
        walkingRouteCoordinatesFromStart.value = null
        _walkingRouteCoordinatesToDestination.value = null  // 도보 경로 초기화
        walkingRouteCoordinatesToDestination.value = null
        _routeGraphicData.value = null
        _allGraphPosList.value = null
        Log.d("reset", "${_walkingRouteCoordinatesFromStart.value}")
    }

    fun searchRoute(startX: Double, startY: Double, endX: Double, endY: Double) {
        Log.d("searchRoute", "${startX},${startY},${endX},${endY}")
        val call = odsayService.searchPubTransPath(
            startX = startX,
            startY = startY,
            endX = endX,
            endY = endY
        )


        call.enqueue(object : Callback<PubTransPathResponse> {
            override fun onResponse(call: Call<PubTransPathResponse>, response: Response<PubTransPathResponse>) {
                if (response.isSuccessful) {
                    val result = response.body()?.result
                    val paths = result?.path ?: emptyList()
                    _pubTransPaths.value = paths
                    Log.d("API_SUCCESS", "경로 검색 성공: $paths")
                } else {
                    _errorMessage.value = "경로 검색에 실패했습니다: ${response.message()}"
                    Log.e("API_ERROR", "Response error: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<PubTransPathResponse>, t: Throwable) {
                _errorMessage.value = "네트워크 오류가 발생했습니다: ${t.message}"
                Log.e("API_ERROR", "Network error: ${t.message}")
            }
        })
    }




    // 좌표로부터 주소를 가져오는 함수
    fun getAddressFromCoordinates(latitude: Double, longitude: Double) {
        val coords = "$longitude,$latitude" // 경도,위도 순서로 문자열 생성
        Log.d("coords", "${coords}")
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
                        val area4 = region.area4.name

                        val address = "$area1 $area2 $area3 $area4"
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
        Log.d("address parts", parts.toString())
        val part0 = parts.getOrNull(0) ?: ""
        val part1 = parts.getOrNull(1) ?: ""
        val part2 = parts.getOrNull(2) ?: ""
        val part3 = parts.getOrNull(3) ?: ""


        return when {
            part0.isNotEmpty() && part1.isNotEmpty() && part3.isNotEmpty() -> "$part0 $part1 $part3"
            part0.isNotEmpty() && part1.isNotEmpty() && part2.isNotEmpty() -> "$part0 $part1 $part2"
            part0.isNotEmpty() && part1.isNotEmpty() -> "$part0 $part1"
            else -> address
        }
    }


    fun searchCafesInArea(area: String) {
        val query = "$area 카페"
        Log.d("query11", query)
        _isLoading.value = true
        val call = naverApiService.searchLocal(
            query = query,
            display = 50,
            start = 1,
            sort = "sim"
        )
        call.enqueue(object : Callback<SearchMapResponse> {
            override fun onResponse(call: Call<SearchMapResponse>, response: Response<SearchMapResponse>) {
                Log.d("search", response.toString())
                _isLoading.value = false
                if (response.isSuccessful) {
                    val items = response.body()?.items?.map { item ->
                        item.copy(title = removeHtmlTags(item.title))
                    } ?: emptyList()
                    Log.d("item", items.toString())
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



    fun resetGraphicDataCounters() {
        expectedGraphicDataCount = 0
        receivedGraphicDataCount = 0
        _allGraphPosList.value = emptyList()
    }

    fun incrementExpectedGraphicDataCount() {
        expectedGraphicDataCount++
    }

    fun getRouteGraphicData(mapObj: String) {
        val mapObject = "0:0@$mapObj"

        Log.d("getRouteGraphicData", "mapObject: $mapObject")

        odsayService.getRouteGraphicData(mapObject).enqueue(object : Callback<RouteGraphicResponse> {
            override fun onResponse(
                call: Call<RouteGraphicResponse>,
                response: Response<RouteGraphicResponse>
            ) {
                if (response.isSuccessful && response.body()?.result != null) {
                    val routeGraphicResponse = response.body()!!
                    val graphPosList = routeGraphicResponse.result.lane.flatMap { lane ->
                        lane.section.flatMap { section ->
                            section.graphPos
                        }
                    }
                    // 기존의 _routeGraphicData 대신에 모든 그래픽 데이터를 수집
                    val currentList = _allGraphPosList.value?.toMutableList() ?: mutableListOf()
                    currentList.addAll(graphPosList)
                    _allGraphPosList.value = currentList

                    // 수신된 그래픽 데이터 카운트 증가
                    receivedGraphicDataCount++
                    checkIfAllGraphicDataReceived()

                    Log.d("RouteGraphicData", "그래픽 데이터 수신 완료: $graphPosList")
                } else {
                    _graphicErrorMessage.value = "노선 그래픽 데이터를 가져오지 못했습니다: ${response.message()}"
                    val errorBody = response.errorBody()?.string()
                    Log.e("API_ERROR", "Response error: $errorBody")
                }
            }

            override fun onFailure(call: Call<RouteGraphicResponse>, t: Throwable) {
                _graphicErrorMessage.value = "네트워크 오류가 발생했습니다: ${t.message}"
                Log.e("API_ERROR", "Network error: ${t.message}")
            }
        })
    }


    private fun checkIfAllGraphicDataReceived() {
        if (receivedGraphicDataCount == expectedGraphicDataCount) {
            // 모든 그래픽 데이터를 수신했을 때

        }
    }

    // 현재 위치에서 대중교통 출발지까지의 도보 경로 요청
    fun getWalkingStartRoute(startX: Double, startY: Double, endX: Double, endY: Double, startName: String, endName: String) {
        Log.d("getWalkingStartRoute", "Start: ($startX, $startY), End: ($endX, $endY)")

        val request = PedestrianRouteRequest(
            startX = startX,
            startY = startY,
            endX = endX,
            endY = endY,
            startName = startName,
            endName = endName,
            searchOption = "10", // 최단거리
            sort = "custom"      // 라인노드, 포인트노드 순
        )

        val call = tmapService.getPedestrianRoute(
            appKey = BuildConfig.TMAP_API_KEY,
            request = request
        )

        call.enqueue(object : Callback<TmapRouteResponse> {
            override fun onResponse(call: Call<TmapRouteResponse>, response: Response<TmapRouteResponse>) {
                if (response.isSuccessful) {
                    val routeResponse = response.body()
                    if (routeResponse != null) {
                        val coordinates = mutableListOf<LatLng>()
                        for (feature in routeResponse.features) {
                            if (feature.geometry is Geometry.LineStringGeometry) {
                                val lineCoords = (feature.geometry as Geometry.LineStringGeometry).coordinates
                                lineCoords.forEach { coord ->
                                    val lon = coord[0]
                                    val lat = coord[1]
                                    coordinates.add(LatLng(lat, lon))
                                }
                            }
                        }
                        // 경로 데이터를 startCoordinates에 저장
                        _walkingRouteCoordinatesFromStart.value = coordinates
                        Log.d("API_SUCCESS", "도보 경로 (출발지) 성공: $coordinates")
                    } else {
                        _errorMessage.value = "도보 경로 응답이 비어 있습니다."
                    }
                } else {
                    _errorMessage.value = "도보 경로 검색 실패: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<TmapRouteResponse>, t: Throwable) {
                _errorMessage.value = "도보 경로 검색 오류: ${t.message}"
            }
        })
    }

    // 대중교통 하차지점에서 카페까지의 도보 경로 요청
    fun getWalkingDestinationRoute(startX: Double, startY: Double, endX: Double, endY: Double, startName: String, endName: String) {
        Log.d("getWalkingDestinationRoute", "Start: ($startX, $startY), End: ($endX, $endY)")

        val request = PedestrianRouteRequest(
            startX = startX,
            startY = startY,
            endX = endX,
            endY = endY,
            startName = "대중교통 하차 지점",
            endName = "카페",
            searchOption = "10", // 최단거리
            sort = "custom"      // 라인노드, 포인트노드 순
        )

        val call = tmapService.getPedestrianRoute(
            appKey = BuildConfig.TMAP_API_KEY,
            request = request
        )

        call.enqueue(object : Callback<TmapRouteResponse> {
            override fun onResponse(call: Call<TmapRouteResponse>, response: Response<TmapRouteResponse>) {
                if (response.isSuccessful) {
                    val routeResponse = response.body()
                    if (routeResponse != null) {
                        val coordinates = mutableListOf<LatLng>()
                        for (feature in routeResponse.features) {
                            if (feature.geometry is Geometry.LineStringGeometry) {
                                val lineCoords = (feature.geometry as Geometry.LineStringGeometry).coordinates
                                lineCoords.forEach { coord ->
                                    val lon = coord[0]
                                    val lat = coord[1]
                                    coordinates.add(LatLng(lat, lon))
                                }
                            }
                        }
                        // 경로 데이터를 destinationCoordinates에 저장
                        _walkingRouteCoordinatesToDestination.value = coordinates
                        Log.d("API_SUCCESS", "도보 경로 (목적지) 성공: $coordinates")
                    } else {
                        _errorMessage.value = "도보 경로 응답이 비어 있습니다."
                    }
                } else {
                    _errorMessage.value = "도보 경로 검색 실패: ${response.message()}"
                }
            }

            override fun onFailure(call: Call<TmapRouteResponse>, t: Throwable) {
                _errorMessage.value = "도보 경로 검색 오류: ${t.message}"
            }
        })
    }

    fun removeHtmlTags(input: String): String {
        return input.replace(Regex("<.*?>"), "")


    }

}
