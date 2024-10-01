package com.example.calmcafeapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calmcafeapp.R
import com.example.calmcafeapp.data.CafeData
import com.example.calmcafeapp.databinding.FragmentRankBinding

class RankFragment : Fragment() {

    private var _binding: FragmentRankBinding? = null
    private val binding get() = _binding!! // ViewBinding 안전하게 접근하기 위해 사용

    private lateinit var adapter: RecyclerViewAdapter // 어댑터 객체 선언

    val mDatas = mutableListOf<CafeData>() // 데이터 리스트

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

        // 데이터 초기화 함수 호출
        initializelist()

        // RecyclerView 초기화 함수 호출
        initCafeRecyclerView()
    }


    // RecyclerView 초기화 함수
    private fun initCafeRecyclerView() {
        adapter = RecyclerViewAdapter() // 어댑터 객체 생성
        adapter.datalist = mDatas // 데이터 넣어줌
        binding.recyclerView.adapter = adapter // 리사이클러뷰에 어댑터 연결
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext()) // 레이아웃 매니저 연결
    }

    // 데이터 리스트 초기화 함수
    private fun initializelist() {
        // 임의의 데이터를 리스트에 추가
        with(mDatas) {
            add(CafeData("", "스타벅스 연남점","보통" ))
            add(CafeData("", "빽다방 연남점","여유" ))
            add(CafeData("", "메가커피 연남점","매우혼잡" ))
            add(CafeData("", "컴포즈커피 연남점","혼잡" ))
            add(CafeData("", "할리스 연남점","보통" ))
            add(CafeData("", "투썸플레이스 연남점","보통" ))

        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 메모리 누수 방지를 위해 ViewBinding 해제
    }



}