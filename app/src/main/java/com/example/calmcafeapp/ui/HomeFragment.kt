package com.example.calmcafeapp.ui

import android.os.Handler
import android.os.Bundle
import android.util.Log
import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Color
import androidx.core.app.ActivityCompat
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.calmcafeapp.R
import com.example.calmcafeapp.base.BaseFragment
import com.example.calmcafeapp.data.LocalItem
import com.example.calmcafeapp.data.Path
import com.example.calmcafeapp.databinding.FragmentHomeBinding
import com.example.calmcafeapp.viewmodel.HomeViewModel
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.*
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.PolylineOverlay
import android.location.Location
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.LifecycleOwner
import com.example.calmcafeapp.MainActivity
import com.example.calmcafeapp.UserActivity
import com.example.calmcafeapp.data.GraphPos
import com.example.calmcafeapp.data.OnRouteStartListener
import com.example.calmcafeapp.data.StorePosDto
import com.naver.maps.map.overlay.OverlayImage

import com.naver.maps.map.util.FusedLocationSource



class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home), OnMapReadyCallback, OnRouteStartListener {
    private lateinit var mapView: MapView
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    private val markerList = mutableListOf<Marker>()
    private val viewModel: HomeViewModel by activityViewModels()

    private var hasFetchedAddress = false
    private var polylineList: MutableList<PolylineOverlay> = mutableListOf()
    private var publicTransportPolylineList: MutableList<PolylineOverlay> = mutableListOf()  // 대중교통 경로
    private var walkingFromStartPolylineList: MutableList<PolylineOverlay> = mutableListOf()
    private var walkingToDestinationPolylineList: MutableList<PolylineOverlay> = mutableListOf()
    private var currentLocation: Location? = null
    private var selectedCafeMarker: Marker? = null
    private var isRouteSearching = false
    private var selectedCafe: StorePosDto? = null
    private var currentSearchStartX: Double? = null
    private var currentSearchStartY: Double? = null
    private var currentSearchEndX: Double? = null
    private var currentSearchEndY: Double? = null



    override fun initStartView() {
        super.initStartView()
        // mapView 초기화
        (activity as UserActivity).binding.navigationUser.visibility = View.VISIBLE
        mapView = binding.mapView ?: throw IllegalStateException("mapView가 null")
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
        (activity as UserActivity).binding.btnBack.setOnClickListener {
            cancelRouteSearch() // 취소 버튼 클릭 시 호출
        }
        // 교통정보 버튼 초기화 및 클릭 리스너 설정
        binding.btnShowNavigator.setOnClickListener {
            showNavigatorBottomSheet()
        }
        mapView.getMapAsync(this)
    }
    override fun initDataBinding() {
        super.initDataBinding()
    }
    override fun initAfterBinding() {
        super.initAfterBinding()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        viewLifecycleOwnerLiveData.observe(this) { owner ->
            owner?.let {
                setupObservers(it)
            }
        }
    }



    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        naverMap.locationSource = locationSource
        naverMap.uiSettings.isLocationButtonEnabled = true

        setupMapListeners()

        viewModel.cafes.value?.let { displayMarkers(it) }

        val locationOverlay = naverMap.locationOverlay
        locationOverlay.isVisible = true  // 위치 오버레이를 보이도록 설정


        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            naverMap.locationTrackingMode = LocationTrackingMode.Follow
            naverMap.addOnLocationChangeListener { location ->
                currentLocation = location
                viewModel.setCurrentLocation(location)
                if (!hasFetchedAddress) {
                    hasFetchedAddress = true
                    val latitude = location.latitude
                    val longitude = location.longitude
                    viewModel.getAddressFromCoordinates(latitude, longitude)
                }
            }
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }
        naverMap.addOnCameraChangeListener { reason, animated ->
            val cameraPosition = naverMap.cameraPosition
            locationOverlay.bearing = cameraPosition.bearing.toFloat()  // 카메라 방향에 따라 아이콘 회전
        }

        setupMapListeners()
    }

    private fun setupMapListeners() {
        naverMap.addOnCameraIdleListener {
            if (!isRouteSearching) {
                val centerPosition = naverMap.cameraPosition.target
                searchCafesAt(centerPosition.latitude, centerPosition.longitude)
            }
        }
    }

    private fun searchCafesAt(latitude: Double, longitude: Double) {
        if (!isRouteSearching) {
            viewModel.getAddressFromCoordinates(latitude, longitude)
        }
    }

    // 권한 요청 결과 처리
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated) {
                // 권한이 거부된 경우
                naverMap.locationTrackingMode = LocationTrackingMode.None
            } else {
                // 권한이 허용된 경우 위치 추적 모드 설정 및 리스너 추가
                naverMap.locationTrackingMode = LocationTrackingMode.Follow

                // 위치 변경 리스너 추가
                naverMap.addOnLocationChangeListener { location ->
                    currentLocation = location
                    if (!hasFetchedAddress) {
                        hasFetchedAddress = true
                        val latitude = location.latitude
                        val longitude = location.longitude
                        // 현재 위치의 주소 가져오기
                        viewModel.getAddressFromCoordinates(latitude, longitude)
                    }
                }
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    // 옵저버 설정
    private fun setupObservers(owner: LifecycleOwner) {
        // 주소 관찰
        viewModel.address.observe(owner) { address ->
            val area = viewModel.extractAreaFromAddress(address)
            Log.d("add2", "${area}")
            // 해당 지역의 카페 요청
            viewModel.fetchCafeLocation(area)
            //viewModel.searchCafesInArea(area)
            viewModel.setStartAddress(address)

        }

        // 카페 목록 관찰
        viewModel.cafes.observe(owner) { cafes ->
            Log.d("FRAGMENT", "Received cafes: $cafes")
            // 마커 표시
            displayMarkers(cafes)
        }

        // 에러 메시지 관찰
        viewModel.errorMessage.observe(owner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        // 로딩 상태 관찰
        viewModel.isLoading.observe(owner) { isLoading ->
            // 예: binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        // 그래픽 에러 메시지 관찰
        viewModel.graphicErrorMessage.observe(owner) { errorMessage ->
            Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }

        // 전체 그래픽 경로 관찰
        viewModel.allGraphPosList.observe(owner) { allGraphPosList ->
            if (!allGraphPosList.isNullOrEmpty()) {
                // 경로가 있으면 경로를 지도에 표시
                displayAllRouteGraphics(allGraphPosList)
            } else if (isRouteSearching && polylineList.isEmpty()) {
                // 경로가 비어 있을 때 메시지 표시
                Handler().postDelayed({
                    if (isRouteSearching && polylineList.isEmpty()) {
                        //Toast.makeText(requireContext(), "로딩 중..", Toast.LENGTH_SHORT).show()
                    }
                }, 1000)  // 1초 정도 기다린 후 다시 확인
            }
        }

        // 도보 경로 관찰 (현재 위치에서 대중교통 출발지까지)
        viewModel.walkingRouteCoordinatesFromStart.observe(owner) { coordinates ->
            if (coordinates != null && coordinates.isNotEmpty()) {
                Log.d("walkingRouteFromStart", "${coordinates}")
                // 현재 위치에서 대중교통 출발지까지의 도보 경로 추가
                displayWalkingRoute(coordinates, isFromStart = true)
            }
        }

        // 도보 경로 관찰 (대중교통 하차 지점에서 카페까지)
        viewModel.walkingRouteCoordinatesToDestination.observe(owner) { coordinates ->
            if (coordinates != null && coordinates.isNotEmpty()) {
                Log.d("walkingRouteToDestination", "${coordinates}")
                // 대중교통 하차 지점에서 카페까지의 도보 경로 추가
                displayWalkingRoute(coordinates, isFromStart = false)
            }
        }

        // 대중교통 경로 관찰
        viewModel.pubTransPaths.observe(owner) { paths ->
            if (paths != null && paths.isNotEmpty()) {
                Log.d("경로 리스트", "경로 리스트: ${paths}")
                val path = paths[0]  // 첫 번째 경로 선택
                displayPublicTransportRoutes(path)
                val publicTransportSubPaths = path.subPath.filter { it.trafficType == 1 || it.trafficType == 2 }
                val firstSubPath = publicTransportSubPaths.firstOrNull()
                val lastSubPath = publicTransportSubPaths.lastOrNull()

                if (firstSubPath != null && lastSubPath != null &&
                    currentSearchStartX != null && currentSearchStartY != null &&
                    currentSearchEndX != null && currentSearchEndY != null
                ) {
                    val transitStartX = firstSubPath.startX ?: currentSearchStartX!!
                    val transitStartY = firstSubPath.startY ?: currentSearchStartY!!
                    viewModel.getWalkingStartRoute(
                        currentSearchStartX!!,
                        currentSearchStartY!!,
                        transitStartX,
                        transitStartY,
                        "현재 위치",
                        "대중교통 출발 지점"
                    )
                    Log.d(
                        "Request",
                        "TMAP 도보 경로 요청: Start(${currentSearchStartX}, ${currentSearchStartY}) -> 대중교통 출발($transitStartX, $transitStartY)"
                    )
                    val transitEndX = lastSubPath.endX ?: currentSearchEndX!!
                    val transitEndY = lastSubPath.endY ?: currentSearchEndY!!
                    viewModel.getWalkingDestinationRoute(
                        transitEndX,
                        transitEndY,
                        currentSearchEndX!!,
                        currentSearchEndY!!,
                        "대중교통 하차 지점",
                        "카페"
                    )
                    Log.d(
                        "Request",
                        "TMAP 도보 경로 요청: 대중교통 하차($transitEndX, $transitEndY) -> End($currentSearchEndX, $currentSearchEndY)"
                    )
                } else {
                }
            }
        }

        viewModel.routeGraphicData.observe(owner) { graphPosList ->
            if (graphPosList != null && graphPosList.isNotEmpty()) {
                val coords = graphPosList.map { LatLng(it.y, it.x) }
                val publicTransportPolyline = PolylineOverlay().apply {
                    this.coords = coords
                    this.color = Color.BLUE
                    this.width = 30
                    this.map = naverMap
                }
                publicTransportPolylineList.add(publicTransportPolyline)
            }
        }



    }
    private fun displayMarkers(cafes: List<StorePosDto>) {
        if (!::naverMap.isInitialized || isRouteSearching) return  // 길찾기 중이거나 naverMap 초기화 안되었을 때 return

        // 기존 마커 제거
        for (marker in markerList) {
            marker.map = null
        }
        markerList.clear()

        // 새로운 마커 추가
        for (cafe in cafes) {
            if (cafe.latitude == 0.0 && cafe.longitude == 0.0) continue

            val latLng = LatLng(cafe.latitude, cafe.longitude)  // latLng 객체 생성

            val marker = Marker().apply {
                position = latLng  // 위에서 생성한 latLng를 사용
                icon = OverlayImage.fromResource(R.drawable.cafe_m)
                width = 100
                height = 100
                map = naverMap
            }

            marker.setOnClickListener {
                showCafeInfo(cafe)

                // 현재 위치의 위경도가 null이 아닌지 확인하고 사용
                val currentLatitude = currentLocation?.latitude
                val currentLongitude = currentLocation?.longitude

                if (currentLatitude != null && currentLongitude != null) {
                    viewModel.fetchCafeDetailInfo(cafe.id, currentLatitude, currentLongitude)
                } else {
                    Toast.makeText(requireContext(), "현재 위치를 확인할 수 없습니다.", Toast.LENGTH_SHORT).show()
                }

                true
            }
            markerList.add(marker)
        }
    }


    private fun displayAllRouteGraphics(allGraphPosList: List<GraphPos>) {
        val coords = allGraphPosList.map { graphPos ->
            LatLng(graphPos.y, graphPos.x)
        }
        // 바깥쪽 흰색 폴리라인
        val outerPolyline = PolylineOverlay().apply {
            this.coords = coords
            this.color = Color.WHITE
            this.width = 35  // 바깥쪽 경로의 너비
            this.map = naverMap
        }
        // 안쪽 파란색 폴리라인
        val innerPolyline = PolylineOverlay().apply {
            this.coords = coords
            this.color = Color.BLUE
            this.width = 20  // 안쪽 경로의 너비
            this.map = naverMap
        }
        // 두 개의 폴리라인을 각각 리스트에 추가
        polylineList.add(outerPolyline)
        polylineList.add(innerPolyline)

        // 카메라 이동: 전체 경로가 보이도록 조정
        val bounds = LatLngBounds.Builder()
        for (coord in coords) {
            bounds.include(coord)
        }
        val latLngBounds = bounds.build()
        val cameraUpdate = CameraUpdate.fitBounds(latLngBounds, 100)  // 패딩 100
        naverMap.moveCamera(cameraUpdate)
    }
    // 카페 정보 표시
    private fun showCafeInfo(cafe: StorePosDto) {
        selectedCafe = cafe  // 선택된 카페 정보 저장
        Log.d("Information", "${cafe}")
        viewModel.setDestinationCafeName(cafe.name ?: "Unknown")

        val cafeDetailFragment = CafeDetailFragment()
        cafeDetailFragment.setTargetFragment(this, 0)
        // 카페 정보를 프래그먼트로 전달
        val bundle = Bundle().apply {
            putString("cafeTitle", cafe.name ?: "Unknown")
            putString("cafeAddress", cafe.address)
            putString("visibility", "visibility" +
                    "")
            // 필요한 다른 정보도 여기에 추가
        }
        cafeDetailFragment.arguments = bundle
        cafeDetailFragment.show(parentFragmentManager, "CafeDetailFragment")
    }
    override fun onRouteStart() {
        Log.d("touch start", "실행")
        selectedCafe?.let { cafe ->
            // 길찾기 시작
            Log.d("touch start", "${cafe}")
            startRouteSearchToCafe(cafe)
            binding.btnShowNavigator.visibility = View.VISIBLE


        } ?: run {
            Toast.makeText(requireContext(), "카페를 선택하지 않았습니다.", Toast.LENGTH_SHORT).show()
        }
    }
    private fun displayPublicTransportRoutes(path: Path) {
        // 기존 대중교통 경로 삭제
        clearPublicTransportRoutes()
        val mapObj = path.info.mapObj

        if (mapObj != null) {
            viewModel.getRouteGraphicData(mapObj)

        }
    }
    private fun clearPublicTransportRoutes() {
        for (polyline in publicTransportPolylineList) {
            polyline.map = null  // 지도에서 제거
        }
        publicTransportPolylineList.clear()  // 리스트 초기화
    }

    private fun displayWalkingRoute(coordinates: List<LatLng>, isFromStart: Boolean) {
        if (isFromStart) {
            // 출발지에서 대중교통 타는 지점까지의 도보 경로 삭제
            walkingFromStartPolylineList.forEach { it.map = null }
            walkingFromStartPolylineList.clear()
            val outerPolyline = PolylineOverlay().apply {
                this.coords = coordinates
                this.color = Color.WHITE
                this.width = 35  // 바깥쪽 경로의 너비
                this.map = naverMap
            }
            val innerPolyline = PolylineOverlay().apply {
                this.coords = coordinates
                this.color = Color.GREEN
                this.width = 20  // 안쪽 경로의 너비
                this.map = naverMap
            }
            walkingFromStartPolylineList.add(outerPolyline)
            walkingFromStartPolylineList.add(innerPolyline)
        } else {

            walkingToDestinationPolylineList.forEach { it.map = null }
            walkingToDestinationPolylineList.clear()

            val outerPolyline = PolylineOverlay().apply {
                this.coords = coordinates
                this.color = Color.WHITE
                this.width = 35
                this.map = naverMap
            }
            val innerPolyline = PolylineOverlay().apply {
                this.coords = coordinates
                this.color = Color.GREEN
                this.width = 20
                this.map = naverMap
            }
            walkingToDestinationPolylineList.add(outerPolyline)
            walkingToDestinationPolylineList.add(innerPolyline)
        }
    }
    private fun clearRoutes() {
        clearPublicTransportRoutes()
        walkingFromStartPolylineList.forEach { polyline ->
            polyline.map = null  // 지도에서 제거
        }
        walkingFromStartPolylineList.clear()

        walkingToDestinationPolylineList.forEach { polyline ->
            polyline.map = null  // 지도에서 제거
        }
        walkingToDestinationPolylineList.clear()
        polylineList.forEach { polyline ->
            polyline.map = null
        }
        polylineList.clear()
        viewModel.resetGraphicDataCounters()
    }

    private fun startRouteSearchToCafe(cafe: StorePosDto) {
        clearRoutes()  // 길찾기 전에 모든 경로 초기화
        viewModel.resetRouteData()

        isRouteSearching = true  // 길찾기 활성화
        hideCafeMarkers()

        // 현재 위치와 선택한 카페에 마커 표시
        showSelectedMarkers(cafe)
        selectedCafe = cafe  // 선택된 카페 저장
        (activity as UserActivity).binding.btnBack.visibility = View.VISIBLE
        if (currentLocation != null) {
            val startX = currentLocation!!.longitude
            val startY = currentLocation!!.latitude
            val endX = cafe.longitude
            val endY = cafe.latitude
            currentSearchStartX = startX
            currentSearchStartY = startY
            currentSearchEndX = endX
            currentSearchEndY = endY

            val distance = FloatArray(1)
            Location.distanceBetween(startY, startX, endY, endX, distance)
            val distanceInMeters = distance[0]

            if (distanceInMeters <= 700) {
                // TMAP 도보 경로 요청
                viewModel.getWalkingStartRoute(startX, startY, endX, endY, "현재 위치", cafe.name?: "Unknown")
                Log.d("Request", "TMAP 도보 경로 요청: Start($startX, $startY) -> End($endX, $endY)")
                //Toast.makeText(requireContext(), "700m 이하일 경우 도보 경로만 제공합니다.", Toast.LENGTH_SHORT).show()
                binding.btnShowNavigator.visibility = View.GONE
            } else {
                // ODSAY 대중교통 경로 요청
                viewModel.searchRoute(startX, startY, endX, endY)
                Log.d("Request", "ODSAY 대중교통 경로 요청: Start($startX, $startY) -> End($endX, $endY)")
                binding.btnShowNavigator.visibility = View.VISIBLE
            }
        } else {
            Toast.makeText(requireContext(), "현재 위치를 확인할 수 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }
    private fun showSelectedMarkers(cafe: StorePosDto) {
        selectedCafeMarker?.map = null
        val latLng = LatLng(cafe.latitude, cafe.longitude)

        selectedCafeMarker = Marker().apply {
            position = latLng
            icon = OverlayImage.fromResource(R.drawable.cafe_m)  // 커스텀 아이콘 사용
            width = 100
            height = 120
            captionText = cafe.name ?: "Unknown"// 마커 아래에 카페 타이틀 표시
            captionTextSize = 14f  // 캡션 텍스트 크기
            captionColor = Color.parseColor("#000000")
            captionRequestedWidth = 200  // 캡션 텍스트 최대 너비
            captionOffset = 10  // 마커와 캡션 사이의 간격 (마커 아래에 위치)
            zIndex = 1000  // 마커가 다른 마커보다 위에 표시되도록 설정
            map = naverMap  // 지도에 마커 추가
        }
    }
    private fun cancelRouteSearch() {
        isRouteSearching = false
        showCafeMarkers()
        selectedCafeMarker?.apply {
            captionText = ""
            map = null
        }
        selectedCafeMarker = null  // 마커 객체 초기화
        selectedCafe = null  // 선택된 카페 정보도 초기화

        // 취소 버튼 숨기기
        (activity as UserActivity).binding.btnBack.visibility = View.GONE
        binding.btnShowNavigator.visibility = View.GONE
        // 모든 경로 삭제
        clearRoutes()
        // ViewModel 데이터 초기화
        viewModel.resetRouteData()  // 데이터만 초기화, 관찰자는 유지
        // 주소 가져오기 플래그 초기화
        hasFetchedAddress = false
        // 현재 검색의 시작과 끝 좌표 초기화
        currentSearchStartX = null
        currentSearchStartY = null
        currentSearchEndX = null
        currentSearchEndY = null
    }
    private fun showCafeMarkers() {
        markerList.forEach { it.map = naverMap }
    }
    private fun hideCafeMarkers() {
        markerList.forEach { it.map = null }
    }
    private fun showNavigatorBottomSheet() {
        val navigatorFragment = NavigatorFragment()
        navigatorFragment.show(parentFragmentManager, "NavigatorFragment")
    }

    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }
}