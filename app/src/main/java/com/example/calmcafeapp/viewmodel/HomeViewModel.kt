package com.example.calmcafeapp.viewmodel

import android.location.Location
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.calmcafeapp.BuildConfig
import com.example.calmcafeapp.api.CafeDetailService
import com.example.calmcafeapp.api.MapService
import com.example.calmcafeapp.apiManager.ApiManager
import com.example.calmcafeapp.data.CafeDetailResponse
import com.example.calmcafeapp.data.CafeDetailResult
import com.example.calmcafeapp.data.CongestionLevelResponse
import com.example.calmcafeapp.data.Geometry
import com.example.calmcafeapp.data.GraphPos
import com.example.calmcafeapp.data.LocalItem
import com.example.calmcafeapp.data.MapResponse
import com.example.calmcafeapp.data.MenuDetailResDto
import com.example.calmcafeapp.data.Path
import com.example.calmcafeapp.data.PedestrianRouteRequest
import com.example.calmcafeapp.data.PointMenuDetailResDto
import com.example.calmcafeapp.data.PubTransPathResponse
import com.example.calmcafeapp.data.PurchaseResponse
import com.example.calmcafeapp.data.ReverseGeocodingResponse
import com.example.calmcafeapp.data.RouteGraphicResponse
import com.example.calmcafeapp.data.SearchHomeResponse
import com.example.calmcafeapp.data.SearchMapResponse
import com.example.calmcafeapp.data.SearchResult
import com.example.calmcafeapp.data.SearchStoreResDto
import com.example.calmcafeapp.data.StorePosDto
import com.example.calmcafeapp.data.TmapRouteResponse
import com.example.calmcafeapp.login.LocalDataSource
import com.naver.maps.geometry.LatLng
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.SimpleDateFormat
import java.util.Locale

class HomeViewModel : ViewModel() {

    private val naverApiService = ApiManager.naverApiService
    private val reverseGeocodingService = ApiManager.naverReverseGeocodingService
    private val odsayService = ApiManager.odsayService
    private val tmapService = ApiManager.tmapService
    private val cafeDetailService = ApiManager.cafeDetailService

    private val _searchResults = MutableLiveData<List<SearchResult>?>()
    val searchResults: MutableLiveData<List<SearchResult>?> get() = _searchResults

    private val _searchStoreResDto = MutableLiveData<List<SearchStoreResDto>>()
    val searchStoreResDto: LiveData<List<SearchStoreResDto>> get() = _searchStoreResDto

    private val _firstCafeLocation = MutableLiveData<LatLng>()
    val firstCafeLocation: LiveData<LatLng> get() = _firstCafeLocation


    private val _userPointsLiveData = MutableLiveData<Int>()
    val userPointsLiveData: LiveData<Int> get() = _userPointsLiveData


    private val _purchaseResultLiveData = MutableLiveData<Boolean>()
    val purchaseResultLiveData: LiveData<Boolean> get() = _purchaseResultLiveData


    private val _pointMenuList = MutableLiveData<List<PointMenuDetailResDto>>()
    val pointMenuList: LiveData<List<PointMenuDetailResDto>> get() = _pointMenuList

    private val _cafeMenuList = MutableLiveData<List<MenuDetailResDto>>()
    val cafeMenuList: LiveData<List<MenuDetailResDto>> get() = _cafeMenuList

    private val _pubTransPaths = MutableLiveData<List<Path>?>()
    val pubTransPaths: MutableLiveData<List<Path>?> get() = _pubTransPaths

    // 노선 그래픽 데이터를 담을 LiveData
    private val _routeGraphicData = MutableLiveData<List<GraphPos>?>()
    val routeGraphicData: MutableLiveData<List<GraphPos>?> get() = _routeGraphicData

    // 에러 메시지를 담을 LiveData
    private val _graphicErrorMessage = MutableLiveData<String>()
    val graphicErrorMessage: LiveData<String> get() = _graphicErrorMessage


    private val _distance = MutableLiveData<Double?>()
    val distance: LiveData<Double?> get() = _distance

    private var expectedGraphicDataCount = 0
    private var receivedGraphicDataCount = 0
    private val _allGraphPosList = MutableLiveData<List<GraphPos>?>()
    val allGraphPosList: MutableLiveData<List<GraphPos>?> get() = _allGraphPosList


    // 카페 목록을 담을 LiveData
    private val _cafes = MutableLiveData<List<StorePosDto>>()
    val cafes: LiveData<List<StorePosDto>> get() = _cafes

    private val _cafeDetail = MutableLiveData<CafeDetailResult?>()
    val cafeDetail: LiveData<CafeDetailResult?> get() = _cafeDetail

    // 포맷팅된 거리와 시간 LiveData
    private val _formattedDistance = MutableLiveData<String>()
    val formattedDistance: LiveData<String> get() = _formattedDistance

    private val _formattedOpeningTime = MutableLiveData<String>()
    val formattedOpeningTime: LiveData<String> get() = _formattedOpeningTime

    private val _formattedClosingTime = MutableLiveData<String>()
    val formattedClosingTime: LiveData<String> get() = _formattedClosingTime


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

    private val _startAddress = MutableLiveData<String>()
    val startAddress: LiveData<String> get() = _startAddress

    private val _destinationCafeName = MutableLiveData<String>()
    val destinationCafeName: LiveData<String> get() = _destinationCafeName

    private val _currentLocation = MutableLiveData<Location?>()
    val currentLocation: LiveData<Location?> = _currentLocation

    // 현재 위치를 업데이트하는 함수 추가
    fun setCurrentLocation(location: Location?) {
        _currentLocation.value = location
    }

    // 시작 주소 설정 메서드
    fun setStartAddress(address: String) {
        _startAddress.value = address
    }

    // 도착지 카페 이름 설정 메서드
    fun setDestinationCafeName(cafeName: String) {
        _destinationCafeName.value = cafeName
    }


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
                    val distance = result?.pointDistance
                    _distance.value = distance
                    _pubTransPaths.value = paths

                    Log.d("API_SUCCESS", "경로 검색 성공: ${result?.path}")
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
                        Log.d("add1", "${region}")
                        val area1 = region.area1.name
                        val area2 = region.area2.name
                        val address = "$area1 $area2"
                        Log.d("add1", "${address}")
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


    // 카페 정보 호출
    fun fetchCafeDetailInfo(storeId: Int, userLatitude: Double, userLongitude: Double){
        _isLoading.value = true
        val call : Call<CafeDetailResponse> = ApiManager.cafeDetailService.getCafeInfo(
            "Bearer " + LocalDataSource.getAccessToken()!!,
            storeId = storeId,
            userLatitude = userLatitude,
           userLongitude = userLongitude
        )

        call.enqueue(object : Callback<CafeDetailResponse> {
            override fun onResponse(call: Call<CafeDetailResponse>, response: Response<CafeDetailResponse>) {

                _isLoading.value = false
                if (response.isSuccessful) {
                    val cafeDetailResponse = response.body()
                    Log.d("cafeDetailResponse", "${response.body()}")
                    if (cafeDetailResponse != null && cafeDetailResponse.isSuccess) {

                        val result = cafeDetailResponse.result
                        Log.d("cafeDetailResponse", "${result}")
                        _cafeDetail.value = result  // LiveData에 설정

                        // 포맷팅된 거리와 시간 설정
                        _formattedDistance.value = formatDistance(result?.distance)
                        _formattedOpeningTime.value = formatTime(result?.openingTime)
                        _formattedClosingTime.value = formatTime(result?.closingTime)
                        val pointMenuList = cafeDetailResponse.result?.pointMenuDetailResDtoList
                        _pointMenuList.value = pointMenuList ?: emptyList()
                        val menuList = cafeDetailResponse.result?.menuDetailResDtoList
                        _cafeMenuList.value = menuList ?: emptyList() // 메뉴 리스트를 LiveData에 저장
                    } else {
                        _errorMessage.value = cafeDetailResponse?.message ?: "응답이 비어 있습니다."
                        Log.e("fetchCafeDetailInfo", "응답 실패: ${cafeDetailResponse?.message}")
                    }
                } else {
                    //_errorMessage.value = "API 호출에 실패했습니다. 코드: ${response.code()}"
                    Log.e("fetchCafeDetailInfo", "응답 실패: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<CafeDetailResponse>, t: Throwable) {
                _isLoading.value = false
                _errorMessage.value = "네트워크 오류가 발생했습니다: ${t.message}"
                Log.e("fetchCafeDetailInfo", "네트워크 오류", t)
            }
        })
    }

    fun formatDistance(distance: Int?): String {
        return distance?.let {
            if (it >= 1000) {
                val km = it / 1000.0
                String.format(Locale.getDefault(), "%.1f km", km)
            } else {
                "$it m"
            }
        } ?: "거리 정보 없음"
    }


    fun formatTime(time: String?): String {
        return try {
            val sdfInput = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val sdfOutput = SimpleDateFormat("HH:mm", Locale.getDefault())
            val date = sdfInput.parse(time ?: "")
            sdfOutput.format(date)
        } catch (e: Exception) {
            "정보 없음"
        }
    }

    fun resetGraphicDataCounters() {
        expectedGraphicDataCount = 0
        receivedGraphicDataCount = 0
        _allGraphPosList.value = emptyList()
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

    fun fetchCafeLocation(userAddress: String) {
        val call = cafeDetailService.fetchStore(userAddress)
        Log.d("userAddress", "${userAddress}")
        call.enqueue(object : Callback<MapResponse> {
            override fun onResponse(call: Call<MapResponse>, response: Response<MapResponse>) {
                if (response.isSuccessful) {
                    Log.d("responseLocation", "${response.body()}")
                    val items = response.body()?.result?.storePosDtoList?.map { storePosDto ->
                        StorePosDto(
                            id = storePosDto.id,
                            congestionLevel = storePosDto.congestionLevel,
                            address = storePosDto.address,
                            latitude = storePosDto.latitude,
                            longitude = storePosDto.longitude,
                            name = storePosDto.name
                        )
                    } ?: emptyList()
                    _cafes.value = items



                    // mapResponse.result로 작업 수행
                } else {
                    _errorMessage.value = "로딩 중.."
                    val errorBody = response.errorBody()?.string()
                    Log.e("API_ERROR", "Response error: $errorBody")
                }
            }

            override fun onFailure(call: Call<MapResponse>, t: Throwable) {
                _errorMessage.value = "네트워크 에러: ${t.message}"
            }
        })
    }

    fun sendCongestionLevel(storeId: Int, congestionValue: Int) {
        _isLoading.value = true
        Log.d("CongestionLevelResponse", "storeId: ${storeId}, ${congestionValue}")
        val call : Call<CongestionLevelResponse> = ApiManager.locationService.createCongestion(
            "Bearer " + LocalDataSource.getAccessToken()!!,
            storeId = storeId,
            congestionValue  = congestionValue)
        call.enqueue(object : Callback<CongestionLevelResponse> {
            override fun onResponse(call: Call<CongestionLevelResponse>, response: Response<CongestionLevelResponse>
            ) {
                Log.d("CongestionLevelResponse", "Response body: ${response.body()}")
                Log.d("CongestionLevelResponse", "Response code: ${response.code()}")
                Log.d("CongestionLevelResponse", "Response message: ${response.message()}")
                Log.d("CongestionLevelResponse", "Error body: ${response.errorBody()?.string()}")
                if (response.isSuccessful) {
                    // 성공 처리
                    Log.d("ViewModel", "혼잡도 전송 성공")
                } else {
                    // 실패 처리
                    Log.e("ViewModel", "혼잡도 전송 실패")
                }
            }

            override fun onFailure(call: Call<CongestionLevelResponse>, t: Throwable) {
                // 에러 처리
                Log.e("ViewModel", "혼잡도 전송 에러: ${t.message}")
            }
        })
    }

    fun purchaseItem(menuId: Int) {
        _isLoading.value = true

        val call: Call<PurchaseResponse> = ApiManager.cafeDetailService.purchaseItem(
            authorization = "Bearer " + LocalDataSource.getAccessToken()!!,
            menuId = menuId
        )

        call.enqueue(object : Callback<PurchaseResponse> {
            override fun onResponse(call: Call<PurchaseResponse>, response: Response<PurchaseResponse>) {
                _isLoading.value = false
                if (response.isSuccessful) {
                    val body = response.body()
                    if (body != null && body.isSuccess) {

                        _purchaseResultLiveData.postValue(true)
                        // 잔여 포인트 업뎃
                        val newPoints = body.result
                        _userPointsLiveData.postValue(newPoints)
                    } else {
                        // 구매 실패 처리
                        _purchaseResultLiveData.postValue(false)
                        _errorMessage.postValue(body?.message ?: "구매에 실패하였습니다.")
                    }
                } else {
                    // 서버 에러 처리
                    _purchaseResultLiveData.postValue(false)
                    _errorMessage.postValue("구매에 실패하였습니다. (서버 에러)")
                }
            }

            override fun onFailure(call: Call<PurchaseResponse>, t: Throwable) {
                _isLoading.value = false
                // 네트워크 에러 처리
                _purchaseResultLiveData.postValue(false)
                _errorMessage.postValue("구매에 실패하였습니다. (네트워크 에러)")
            }
        })
    }

    fun searchHome(userLatitude: Double, userLongitude: Double, query: String) {
        val call: Call<SearchHomeResponse> = ApiManager.naverApiService.searchHome(
            "Bearer " + LocalDataSource.getAccessToken()!!,
            userLatitude = userLatitude,
            userLongitude = userLongitude,
            query = query
        )

        call.enqueue(object : Callback<SearchHomeResponse> {
            override fun onResponse(
                call: Call<SearchHomeResponse>,
                response: Response<SearchHomeResponse>
            ) {
                Log.d("HTTP Response", "Code: ${response.code()}, Message: ${response.message()}")
                if (response.isSuccessful) {
                    val body = response.body()
                    Log.d("Response Body", "${body}")

                    if (body != null && body.isSuccess) {
                        val searchStoreResDtoList =
                            body.result?.searchStoreResDtoList ?: emptyList()
                        val searchResults = body.result
                        _searchStoreResDto.postValue(searchStoreResDtoList)
                        _searchResults.postValue(listOf(searchResults))
                        if (searchStoreResDtoList.isNotEmpty()) {
                            val firstCafe = searchStoreResDtoList[0]
                            _firstCafeLocation.postValue(
                                LatLng(
                                    body.result?.latitude ?: 0.0,
                                    body.result?.longitude ?: 0.0
                                )
                            )
                        }
                    } else {
                        Log.e(
                            "Response Error",
                            "isSuccess: ${body?.isSuccess}, Message: ${body?.message}"
                        )
                        _errorMessage.postValue(body?.message ?: "검색 결과를 가져오지 못했습니다.")
                    }
                } else {
                    val errorBody = response.errorBody()?.string()
                    Log.e("Server Error", "Code: ${response.code()}, Error Body: $errorBody")
                    _errorMessage.postValue("검색 결과를 가져오지 못했습니다. 서버 에러.")
                }
            }

            override fun onFailure(call: Call<SearchHomeResponse>, t: Throwable) {
                Log.e("Network Error", "Error: ${t.message}")
                _errorMessage.postValue("네트워크 오류가 발생했습니다.")
            }
        })

    }
}
