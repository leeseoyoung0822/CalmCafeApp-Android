package com.example.calmcafeapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import android.widget.Button
import androidx.appcompat.widget.SearchView
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calmcafeapp.data.CafeData
import com.example.calmcafeapp.databinding.FragmentRankBinding
import com.example.calmcafeapp.R

class RankFragment : Fragment() {

    private var _binding: FragmentRankBinding? = null
    private val binding get() = _binding!! // ViewBinding 안전하게 접근하기 위해 사용

    private lateinit var adapter: CafeRecyclerViewAdapter // 어댑터 객체 선언
    private val mDatas = mutableListOf<CafeData>() // 데이터 리스트

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentRankBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 초기화
        initCafeRecyclerView()

        // 디폴트로 btn1 클릭 이벤트와 동일한 데이터를 로드
        loadData1()

        setButtonSelected(binding.btn1)


        binding.btn1.setOnClickListener{
            loadData1()
            setButtonSelected(binding.btn1) // 버튼1을 선택 상태로
        }
        binding.btn2.setOnClickListener {
            loadData2() // 버튼2 클릭 시 데이터 변경
            setButtonSelected(binding.btn2) // 버튼2을 선택 상태로
        }

        binding.btn3.setOnClickListener {
            loadData3() // 버튼3 클릭 시 데이터 변경
            setButtonSelected(binding.btn3) // 버튼3을 선택 상태로

        }
        // SearchView에서 텍스트가 변경될 때마다 필터링 수행
        setOnQueryTextListener()
    }

    // SearchView 초기화 및 검색 기능 추가
    private fun setOnQueryTextListener() {
        binding.searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                // 검색 텍스트 제출 시 동작 (필요에 따라 처리)
                return false
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                adapter.performSearch(newText ?: "") // 어댑터의 필터링 함수 호출
                return true
            }
        })
    }




    // RecyclerView 초기화 함수
    private fun initCafeRecyclerView() {
        adapter = CafeRecyclerViewAdapter(mDatas) // 어댑터 객체 생성
        binding.cafeRecyclerView.adapter = adapter // 리사이클러뷰에 어댑터 연결
        binding.cafeRecyclerView.layoutManager = LinearLayoutManager(requireContext()) // 레이아웃 매니저 연결
    }

    // 선택된 버튼의 선택 상태를 유지하고 나머지 버튼의 선택을 해제하는 함수
    private fun setButtonSelected(selectedButton:android.widget.Button) {
        // 모든 버튼의 선택 상태 해제
        binding.btn1.isSelected = false
        binding.btn2.isSelected = false
        binding.btn3.isSelected = false

        // 선택된 버튼만 선택 상태로 설정
        selectedButton.isSelected = true
    }

    // 버튼 1에 대한 더미 데이터 로드
    private fun loadData1() {
        mDatas.clear() // 기존 데이터 초기화
        mDatas.addAll(listOf(
            CafeData("", "starbucks 연남점", "보통"),
            CafeData("", "빽다방 연남점", "여유"),
            CafeData("", "메가커피 연남점", "혼잡") ,
            CafeData("", "스타벅스 연남점", "보통"),
            CafeData("", "빽다방 연남점", "여유"),
            CafeData("", "메가커피 연남점", "혼잡")
        ))
        adapter.notifyDataSetChanged() // 어댑터에 데이터 변경 통보
    }
    // 버튼 2에 대한 더미 데이터 로드
    private fun loadData2() {
        mDatas.clear()
        mDatas.addAll(listOf(
            CafeData("", "카페A", "혼잡"),
            CafeData("", "카페B", "매우혼잡"),
            CafeData("", "카페C", "보통"),
            CafeData("", "스타벅스 연남점", "보통"),
            CafeData("", "빽다방 연남점", "여유"),
            CafeData("", "메가커피 연남점", "혼잡")
        ))
        adapter.notifyDataSetChanged()
    }

    // 버튼 3에 대한 더미 데이터 로드
    private fun loadData3() {
        mDatas.clear()
        mDatas.addAll(listOf(
            CafeData("", "카페X", "매우여유"),
            CafeData("", "카페Y", "여유"),
            CafeData("", "카페Z", "혼잡"),
            CafeData("", "스타벅스 연남점", "보통"),
            CafeData("", "빽다방 연남점", "여유"),
            CafeData("", "메가커피 연남점", "혼잡")
        ))
        adapter.notifyDataSetChanged()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 메모리 누수 방지를 위해 ViewBinding 해제
    }



}