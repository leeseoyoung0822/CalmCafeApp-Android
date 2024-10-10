package com.example.calmcafeapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.GridLayoutManager
import com.example.calmcafeapp.R
import com.example.calmcafeapp.databinding.FragmentFavoriteBinding

class FavoriteFragment : Fragment() {

    private var _binding: FragmentFavoriteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentFavoriteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView 초기화
        binding.recyclerView.layoutManager = GridLayoutManager(requireContext(), 2)

        // RecyclerView 어댑터 설정 (임시 데이터 설정)
        val initialCafeList = mutableListOf(
            Cafe("카페 A", "서울특별시 00구", "혼잡도: 보통", R.drawable.favorite),
            Cafe("카페 B", "서울특별시 00구", "혼잡도: 혼잡", R.drawable.favorite),
            Cafe("카페 B", "서울특별시 00구", "혼잡도: 혼잡", R.drawable.favorite),
            Cafe("카페 B", "서울특별시 00구", "혼잡도: 혼잡", R.drawable.favorite),
            Cafe("카페 B", "서울특별시 00구", "혼잡도: 혼잡", R.drawable.favorite),
            Cafe("카페 B", "서울특별시 00구", "혼잡도: 혼잡", R.drawable.favorite),
            Cafe("카페 B", "서울특별시 00구", "혼잡도: 혼잡", R.drawable.favorite),
        )

        val adapter = CafeAdapter(initialCafeList)
        binding.recyclerView.adapter = adapter
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
