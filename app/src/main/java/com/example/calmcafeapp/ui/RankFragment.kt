package com.example.calmcafeapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import android.widget.Button
import androidx.appcompat.widget.SearchView
import androidx.fragment.app.viewModels
import androidx.lifecycle.Observer
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.viewpager2.widget.ViewPager2
import com.example.calmcafeapp.databinding.FragmentRankBinding
import com.example.calmcafeapp.R
import com.example.calmcafeapp.viewmodel.RankViewModel
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class RankFragment : Fragment() {

    private lateinit var binding: FragmentRankBinding
    private val rankViewModel: RankViewModel by viewModels()
    private lateinit var adapter: CafeRecyclerViewAdapter // 어댑터 객체 선언
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

        initCafeRecyclerView()

        // 기본으로 '전국' 지역과 '실시간 방문자 수' 데이터를 로드
        rankViewModel.fetchRealTimeRanking("전국")  // 초기 지역은 '전국', 카테고리는 '실시간 방문자 수'

        // 기본 선택 버튼과 제목 설정
        setButtonSelected(binding.btn1)
        binding.categoryTitle.text = "실시간 방문자 수 TOP 랭킹"

        // ViewModel의 데이터 변화 관찰
        rankViewModel.storeList.observe(viewLifecycleOwner, Observer { storeList ->
            Log.d("RankFragment", "Observed data for RecyclerView: $storeList")
            adapter.updateData(storeList)
        })

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

        /*  // 검색버튼 누르면
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
        adapter = CafeRecyclerViewAdapter(mutableListOf()){ storeId ->
            val bundle = Bundle().apply {
                putInt("storeId", storeId)
            }
            val detailFragment = CafeDetailFragment()
            detailFragment.arguments = bundle
            detailFragment.show(parentFragmentManager, "CafeDetailFragment")  // BottomSheetDialogFragment로 열기
        }
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
                        val selectedRegion = it.text.toString()  // Tab의 텍스트를 가져와서 지역 변수에 할당
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


}