package com.example.calmcafeapp.ui

import android.os.Bundle
import android.util.Log
import android.Manifest
import android.app.AlertDialog
import android.content.pm.PackageManager
import androidx.core.app.ActivityCompat
import android.view.View
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.fragment.app.viewModels
import com.example.calmcafeapp.MainActivity
import com.example.calmcafeapp.R
import com.example.calmcafeapp.base.BaseFragment
import com.example.calmcafeapp.data.LocalItem
import com.example.calmcafeapp.databinding.FragmentHomeBinding
import com.example.calmcafeapp.viewmodel.HomeViewModel
import com.naver.maps.geometry.Coord
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import com.naver.maps.map.*
import com.naver.maps.map.MapView
import com.naver.maps.map.overlay.Marker
import com.naver.maps.geometry.Tm128

class HomeFragment : BaseFragment<FragmentHomeBinding>(R.layout.fragment_home), OnMapReadyCallback {

    private lateinit var mapView: MapView
    private lateinit var locationSource: FusedLocationSource
    private lateinit var naverMap: NaverMap
    private val markerList = mutableListOf<Marker>()
    private val viewModel: HomeViewModel by viewModels()



    // 레이아웃이 화면에 표시되기 직후에 호출, 뷰나 액티비티의 속성을 초기화하는 데 사용
    // ex) RecyclerView 설정, Toolbar 초기화, Drawer 설정의 초기 설정
    override fun initStartView() {
        super.initStartView()
        (activity as MainActivity).binding.toolbar.visibility = View.GONE
        mapView = binding.mapView ?: throw IllegalStateException("mapView가 null")
        // FusedLocationSource 초기화
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)

        // 지도 비동기 호출
        mapView.getMapAsync(this)

        setupObservers()
    }

    // 데이터 바인딩을 설정하는 데 사용
    // ex) ViewModel과의 연결, LiveData 관찰자 설정
    override fun initDataBinding() {
        super.initDataBinding()
    }

    // 데이터 바인딩 이후에 추가로 설정해야 할 것들을 처리
    // ex) 클릭 리스너, 추가적인 이벤트 처리 등을 이곳에서 설정
    override fun initAfterBinding() {
        super.initAfterBinding()
    }


    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
        mapView.onPause()
        super.onPause()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onStop() {
        mapView.onStop()
        super.onStop()
    }

    override fun onDestroyView() {
        mapView.onDestroy()
        super.onDestroyView()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        // 위치 소스 설정
        naverMap.locationSource = locationSource

        // 현 위치 버튼 활성화
        naverMap.uiSettings.isLocationButtonEnabled = true

        // 위치 추적 모드 설정
        naverMap.locationTrackingMode = LocationTrackingMode.Follow

        // 위치 추적 모드 설정
        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            naverMap.locationTrackingMode = LocationTrackingMode.Follow
        } else {
            ActivityCompat.requestPermissions(requireActivity(), arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }

        searchCafesInCurrentArea()
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        if (locationSource.onRequestPermissionsResult(requestCode, permissions, grantResults)) {
            if (!locationSource.isActivated) {
                // 권한이 거부된 경우
                naverMap.locationTrackingMode = LocationTrackingMode.None
            }
            return
        }
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
    }

    // 옵저버
    private fun setupObservers() {
        // 주소 관찰
        viewModel.address.observe(viewLifecycleOwner) { address ->
            val area = viewModel.extractAreaFromAddress(address)
            Log.d("add2", "${area}")
            viewModel.searchCafesInArea(area)
        }

        // 카페 목록 관찰
        viewModel.cafes.observe(viewLifecycleOwner) { items ->
            displayMarkers(items)
        }

        // 에러 메시지 관찰
        viewModel.errorMessage.observe(viewLifecycleOwner) { message ->
            Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
        }

        // 로딩 상태 관찰
        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            // 로딩 상태에 따라 ProgressBar 표시 처리
        }
    }


    private fun displayMarkers(items: List<LocalItem>) {
        // 기존의 마커를 모두 제거
        for (marker in markerList) {
            marker.map = null
        }
        markerList.clear()

        for (item in items) {
            // 좌표 변환
            val tm128Coord = Tm128(item.mapx.toDouble(), item.mapy.toDouble())
            val latLng = tm128Coord.toLatLng()

            val marker = Marker()
            marker.position = latLng
            marker.map = naverMap

            // 마커 리스트에 추가
            markerList.add(marker)

            // 마커 클릭 리스너 설정
            marker.setOnClickListener {
                showCafeInfo(item)
                true
            }
        }
    }



    private fun searchCafesInCurrentArea() {
        val cameraPosition = naverMap.cameraPosition
        val centerLatLng = cameraPosition.target

        // ViewModel의 함수 호출
        viewModel.getAddressFromCoordinates(centerLatLng.latitude, centerLatLng.longitude)
    }


    private fun showCafeInfo(item: LocalItem) {
        AlertDialog.Builder(requireContext())
            .setTitle(item.title)
            .setMessage(item.address)
            .setPositiveButton("확인", null)
            .show()
    }




    companion object {
        private const val LOCATION_PERMISSION_REQUEST_CODE = 1000
    }

}