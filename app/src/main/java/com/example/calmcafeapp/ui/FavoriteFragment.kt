package com.example.calmcafeapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calmcafeapp.R

class FavoriteFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // fragment_favorite.xml과 연결
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 초기화
        val recyclerView = view.findViewById<RecyclerView>(R.id.recyclerView)

        // GridLayoutManager 설정: 열의 개수를 2로 설정
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        // RecyclerView 어댑터 설정 (임시 데이터 설정)
        val initialCafeList = mutableListOf(
            Cafe("카페 A", "서울특별시 00구", "혼잡도: 보통", R.drawable.logo),
            Cafe("카페 B", "서울특별시 00구", "혼잡도: 혼잡", R.drawable.favorite)
        )

        val adapter = CafeAdapter(initialCafeList)
        recyclerView.adapter = adapter
    }
}
