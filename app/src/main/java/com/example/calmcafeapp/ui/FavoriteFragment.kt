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
            CafeData(R.drawable.coupon_img, "스타벅스 연남점", "혼잡도: 보통"),
            CafeData(R.drawable.cafe_img2, "투썸플레이스 홍대점", "혼잡도: 혼잡"),
            CafeData(R.drawable.cafe_img5, "커피 빈 용산점역", "혼잡도: 혼잡"),
            CafeData(R.drawable.cafe_img4, "블루팟", "혼잡도: 한산"),
            CafeData(R.drawable.cafe_img6, "라플란드 카페", "혼잡도: 혼잡"),
            CafeData(R.drawable.sample_cafe_img, "빽다방 행신역점", "혼잡도: 보통"),
            CafeData(R.drawable.cafe_img1, "아말룬 커피", "혼잡도: 보통")
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
