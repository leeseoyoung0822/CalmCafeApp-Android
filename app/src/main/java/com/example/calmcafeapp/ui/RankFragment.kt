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
import com.example.calmcafeapp.data.CafeData
import com.example.calmcafeapp.databinding.FragmentRankBinding
import com.example.calmcafeapp.R

class RankFragment : Fragment() {

    private lateinit var binding: FragmentRankBinding

    private lateinit var adapter: CafeRecyclerViewAdapter // 어댑터 객체 선언


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

        // RecyclerView 초기화
        initCafeRecyclerView()

        // 디폴트로 btn1 클릭 이벤트와 동일한 데이터를 로드
        loadData1()

        setButtonSelected(binding.btn1)
        binding.categoryTitle.text = "실시간 방문 수 TOP 랭킹" // 초기 텍스트 설정


        binding.btn1.setOnClickListener{
            loadData1()
            setButtonSelected(binding.btn1)
            binding.categoryTitle.text = "실시간 방문 수 TOP 랭킹" // 초기 텍스트 설정  버튼1을 선택 상태로
        }
        binding.btn2.setOnClickListener {
            loadData2() // 버튼2 클릭 시 데이터 변경
            setButtonSelected(binding.btn2) // 버튼2을 선택 상태로
            binding.categoryTitle.text = "실시간 테이크아웃 TOP 랭킹"
        }

        binding.btn3.setOnClickListener {
            loadData3() // 버튼3 클릭 시 데이터 변경
            setButtonSelected(binding.btn3) // 버튼3을 선택 상태로
            binding.categoryTitle.text = "실시간 할인률 TOP 랭킹"
        }

        binding.btn4.setOnClickListener {
            loadData3() // 버튼3 클릭 시 데이터 변경
            setButtonSelected(binding.btn4) // 버튼3을 선택 상태로
            binding.categoryTitle.text = "실시간 즐겨찾기 TOP 랭킹"

        }

        // RankFragment의 onViewCreated() 메서드 내에 추가
        binding.searchBtn.setOnClickListener {
            // SearchFragment로 이동
            val searchFragment = SearchFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.container_main, searchFragment)
                .addToBackStack(null)  // 뒤로 가기 버튼으로 돌아올 수 있게 백스택에 추가
                .commit()

        }
    }
    // RecyclerView 초기화 함수
    private fun initCafeRecyclerView() {
        adapter = CafeRecyclerViewAdapter(mutableListOf()) // 어댑터 객체 생성
        binding.cafeRecyclerView.adapter = adapter // 리사이클러뷰에 어댑터 연결
        binding.cafeRecyclerView.layoutManager = LinearLayoutManager(requireContext()) // 레이아웃 매니저 연결
    }

    // 선택된 버튼의 선택 상태를 유지하고 나머지 버튼의 선택을 해제하는 함수
    private fun setButtonSelected(selectedButton:android.widget.Button) {
        // 모든 버튼의 선택 상태 해제
        binding.btn1.isSelected = false
        binding.btn2.isSelected = false
        binding.btn3.isSelected = false
        binding.btn4.isSelected = false

        // 선택된 버튼만 선택 상태로 설정
        selectedButton.isSelected = true
    }

    // 버튼 1에 대한 더미 데이터 로드
    private fun loadData1() {
        val data1 = listOf(
            CafeData("", "starbucks 연남점", "보통"),
            CafeData("", "빽다방 연남점", "여유"),
            CafeData("", "메가커피 연남점", "혼잡") ,
            CafeData("", "스타벅스 연남점", "보통"),
            CafeData("", "빽다방 연남점", "여유"),
            CafeData("", "메가커피 연남점", "혼잡")
        )
        adapter.updateData(data1) // 어댑터에 데이터 변경 통보
    }
    // 버튼 2에 대한 더미 데이터 로드
    private fun loadData2() {
        val data2  = listOf(
            CafeData("", "카페A", "혼잡"),
            CafeData("", "카페B", "매우혼잡"),
            CafeData("", "카페C", "보통"),
            CafeData("", "스타벅스 연남점", "보통"),
            CafeData("", "빽다방 연남점", "여유"),
            CafeData("", "메가커피 연남점", "혼잡")
        )
        adapter.updateData(data2)
    }

    // 버튼 3에 대한 더미 데이터 로드
    private fun loadData3() {
        val data3 = listOf(
            CafeData("", "카페X", "매우여유"),
            CafeData("", "카페Y", "여유"),
            CafeData("", "카페Z", "혼잡"),
            CafeData("", "스타벅스 연남점", "보통"),
            CafeData("", "빽다방 연남점", "여유"),
            CafeData("", "메가커피 연남점", "혼잡")
        )
        adapter.updateData(data3)
    }

}