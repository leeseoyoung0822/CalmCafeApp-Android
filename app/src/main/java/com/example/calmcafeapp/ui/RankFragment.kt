package com.example.calmcafeapp.ui

import android.os.Bundle
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
import com.example.calmcafeapp.data.CafeData
import com.example.calmcafeapp.databinding.FragmentRankBinding
import com.example.calmcafeapp.R
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class RankFragment : Fragment() {

    private lateinit var binding: FragmentRankBinding
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

        setupTableLayout()

        // 디폴트로 '실시간 방문자 수' 버튼 클릭 처리

        setButtonSelected(binding.btn1)
        binding.categoryTitle.text = "실시간 방문자 수 TOP 랭킹"

        binding.btn1.setOnClickListener{
            val selectedRegion = getCurrentRegion()

            setButtonSelected(binding.btn1)
            binding.categoryTitle.text = "실시간 방문자 수 TOP 랭킹" // 초기 텍스트 설정  버튼1을 선택 상태로
        }
        binding.btn2.setOnClickListener {
            val selectedRegion = getCurrentRegion()

            setButtonSelected(binding.btn2) // 버튼2을 선택 상태로
            binding.categoryTitle.text = "누적 방문자 수 TOP 랭킹"
        }

        binding.btn3.setOnClickListener {
            val selectedRegion = getCurrentRegion()

            setButtonSelected(binding.btn3) // 버튼3을 선택 상태로
            binding.categoryTitle.text = "즐겨찾기 TOP 랭킹"
        }

        binding.updateBtn.setOnClickListener {
            val selectedRegion = getCurrentRegion() // 현재 선택된 지역
            val selectedButton = getSelectedButton() // 현재 선택된 버튼

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
        adapter = CafeRecyclerViewAdapter(mutableListOf()) // 어댑터 객체 생성
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