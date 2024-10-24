package com.example.calmcafeapp.ui

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calmcafeapp.R
import com.example.calmcafeapp.base.BaseFragment
import com.example.calmcafeapp.data.CafeData
import com.example.calmcafeapp.databinding.FragmentFavoriteBinding

class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>(R.layout.fragment_favorite) {

    override fun initStartView() {
        // RecyclerView 초기화
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        // RecyclerView 어댑터 설정 (임시 데이터 설정)
        val initialCafeList = mutableListOf(
            CafeData("R.drawable.favorite", "카페 A", "혼잡도: 보통"),
            CafeData("R.drawable.favorite", "카페 B", "혼잡도: 혼잡"),
            CafeData("R.drawable.favorite", "카페 C", "혼잡도: 혼잡"),
            CafeData("R.drawable.favorite", "카페 D", "혼잡도: 혼잡"),
            CafeData("R.drawable.favorite", "카페 E", "혼잡도: 혼잡"),
            CafeData("R.drawable.favorite", "카페 F", "혼잡도: 혼잡"),
            CafeData("R.drawable.favorite", "카페 G", "혼잡도: 혼잡")
        )

        val adapter = CafeAdapter(initialCafeList)
        binding.recyclerView.adapter = adapter
    }

    override fun initAfterBinding() {
        // 뒤로가기 버튼 설정
        binding.backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
