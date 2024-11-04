package com.example.calmcafeapp.ui

import android.location.Location
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.calmcafeapp.databinding.FragmentRankBinding
import com.example.calmcafeapp.R
import com.example.calmcafeapp.UserActivity
import com.example.calmcafeapp.data.OnRouteStartListener
import com.example.calmcafeapp.data.StorePosDto
import com.example.calmcafeapp.viewmodel.HomeViewModel
import com.example.calmcafeapp.viewmodel.RankViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class RankFragment : Fragment(), OnRouteStartListener {

    private lateinit var binding: FragmentRankBinding
    private lateinit var adapter: CafeRecyclerViewAdapter // 어댑터 객체 선언
    private val rankViewModel: RankViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var userLatitude: Double = 37.650579
    private var userLongitude: Double= 126.867010
    private var currentLocation: Location? = null

    private val regions = listOf(
        "전국", "서울", "경기",
        "인천", "제주", "부산",
        "대구", "광주", "대전",
        "울산", "경상", "전라",
        "강원", "충청", "세종")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRankBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        (activity as UserActivity).binding.navigationUser.visibility = View.VISIBLE

        // ViewModel의 현재 위치 LiveData 관찰
        homeViewModel.currentLocation.observe(viewLifecycleOwner) { location ->
            if (location != null) {
                userLatitude = location.latitude
                userLongitude = location.longitude

            } else {
                // 위치 정보를 가져올 수 없는 경우 처리
                userLatitude = 37.650579
                userLongitude = 126.867010
            }
        }

        // fusedLocationClient 초기화
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireContext())
        initCafeRecyclerView()

        // 기본으로 '전국' 지역과 '실시간 방문자 수' 데이터를 로드
        rankViewModel.fetchRealTimeRanking("전국")  // 초기 지역은 '전국', 카테고리는 '실시간 방문자 수'

        setButtonSelected(binding.btn1)
        binding.categoryTitle.text = "실시간 방문자 수 TOP 랭킹"

        // 데이터 변경 감지 및 업데이트
        rankViewModel.storeList.observe(viewLifecycleOwner) { storeList ->
            adapter.updateData(storeList)
        }

        // 즐겨찾기 상태 변경을 감지하여 어댑터 업데이트
        rankViewModel.favoriteStoreId.observe(viewLifecycleOwner) { updatedStoreId ->
            if (updatedStoreId != null) {
                adapter.updateLikeStatus(updatedStoreId) // 해당 아이템만 업데이트
                rankViewModel.resetFavoriteStoreId() // 초기화하여 중복 반영 방지
            }
        }

        setupTableLayout()

        binding.btn1.setOnClickListener{
            val selectedRegion = getCurrentRegion()
            rankViewModel.fetchRealTimeRanking(selectedRegion)
            setButtonSelected(binding.btn1)
            binding.categoryTitle.text = "실시간 방문자 수 TOP 랭킹" // 초기 텍스트 설정  버튼1을 선택 상태로
        }
        binding.btn2.setOnClickListener {
            val selectedRegion = getCurrentRegion()
            rankViewModel.fetchTotalRanking(selectedRegion)
            setButtonSelected(binding.btn2) // 버튼2을 선택 상태로
            binding.categoryTitle.text = "누적 방문자 수 TOP 랭킹"
        }

        binding.btn3.setOnClickListener {
            val selectedRegion = getCurrentRegion()
            rankViewModel.fetchFavoriteRanking(selectedRegion)
            setButtonSelected(binding.btn3) // 버튼3을 선택 상태로
            binding.categoryTitle.text = "즐겨찾기 TOP 랭킹"
        }
        binding.updateBtn.setOnClickListener {
            val selectedRegion = getCurrentRegion() // 현재 선택된 지역
            val selectedButton = getSelectedButton() // 현재 선택된 버튼

            // 데이터 로드 (선택된 버튼과 지역에 맞춰 호출)
            when (selectedButton) {
                "실시간 방문자 수" -> rankViewModel.fetchRealTimeRanking(selectedRegion)
                "누적 방문자 수" -> rankViewModel.fetchTotalRanking(selectedRegion)
                "즐겨찾기" -> rankViewModel.fetchFavoriteRanking(selectedRegion)
            }

            // RecyclerView를 맨 위로 스크롤
            binding.cafeRecyclerView.scrollToPosition(0)
        }

        /*
        binding.searchBtn.setOnClickListener {
            // SearchFragment로 이동
            val searchFragment = SearchFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.container_main, searchFragment)
                .addToBackStack(null)  // 뒤로 가기 버튼으로 돌아올 수 있게 백스택에 추가
                .commit()

        }*/
    }
    // RecyclerView 초기화 함수
    private fun initCafeRecyclerView() {
        // RankFragment에서 어댑터를 초기화할 때 클릭 리스너를 설정하여, 클릭 시 CafeDetailFragment로 이동하게 만듦
        adapter = CafeRecyclerViewAdapter(
            mItem = mutableListOf(),
            onItemClick = { storeId, _, _ ->
                showCafeDetailFragment(storeId,userLatitude,userLongitude)
            },
            onFavoriteClick = { storeId, isFavorite ->
                if (isFavorite) {
                    rankViewModel.removeFavorite(storeId)
                } else {
                    rankViewModel.addFavorite(storeId)
                }
            }
        )

        // 어댑터에 즐겨찾기 상태 업데이트 관찰 설정
        binding.cafeRecyclerView.adapter = adapter // 리사이클러뷰에 어댑터 연결
        binding.cafeRecyclerView.layoutManager = LinearLayoutManager(requireContext()) // 레이아웃 매니저 연결
    }

    // TabLayout 설정 및 지역 선택 시 데이터를 로드하는 함수
    private fun setupTableLayout() {
        binding.tabLayout.apply {
            regions.forEach { region ->
                addTab(newTab().setText(region))
            }

            addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.let {
                        val selectedRegion = it.text.toString()
                        val selectedButton = getSelectedButton()

                        // 선택된 버튼에 따라 적절한 데이터를 로드합니다.
                        when (selectedButton) {
                            "실시간 방문자 수" -> rankViewModel.fetchRealTimeRanking(selectedRegion)
                            "누적 방문자 수" -> rankViewModel.fetchTotalRanking(selectedRegion)
                            "즐겨찾기" -> rankViewModel.fetchFavoriteRanking(selectedRegion)
                        }
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }
    }

    // 선택된 버튼을 반환하는 함수
    private fun getSelectedButton(): String {
        return when {
            binding.btn1.isSelected -> "실시간 방문자 수"
            binding.btn2.isSelected -> "누적 방문자 수"
            binding.btn3.isSelected -> "즐겨찾기"
            else -> "실시간 방문자 수"
        }
    }


    // 선택된 버튼의 선택 상태를 유지하고 나머지 버튼의 선택을 해제하는 함수
    private fun setButtonSelected(selectedButton:Button) {
        // 모든 버튼의 선택 상태 해제
        binding.btn1.isSelected = false
        binding.btn2.isSelected = false
        binding.btn3.isSelected = false

        // 선택된 버튼만 선택 상태로 설정
        selectedButton.isSelected = true
    }

    // 현재 선택된 지역을 반환하는 함수
    private fun getCurrentRegion(): String {
        return binding.tabLayout.getTabAt(binding.tabLayout.selectedTabPosition)?.text.toString()
    }

    private fun showCafeDetailFragment(storeId: Int, userLatitude: Double, userLongitude: Double) {
        if (userLatitude != null && userLongitude != null) {
            homeViewModel.fetchCafeDetailInfo(storeId, userLatitude, userLongitude)
            Log.d("showCafeDetailFragment", "$storeId, $userLatitude, $userLongitude")
            val cafeDetailFragment = CafeDetailFragment()
            cafeDetailFragment.setTargetFragment(this, 0)

            val bundle = Bundle().apply {
                putString("visibility", "gone")
           }
            cafeDetailFragment.arguments = bundle
            cafeDetailFragment.show(parentFragmentManager, "CafeDetailFragment")
        } else {
            Toast.makeText(requireContext(), "현재 위치를 확인할 수 없습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onRouteStart() {

        Toast.makeText(requireContext(), "랭킹에서는 길 찾기 불가", Toast.LENGTH_SHORT).show()
    }


}

