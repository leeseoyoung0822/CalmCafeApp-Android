package com.example.calmcafeapp.ui

import android.os.Bundle
import androidx.recyclerview.widget.GridLayoutManager
import com.example.calmcafeapp.MainActivity
import com.example.calmcafeapp.R
import com.example.calmcafeapp.UserActivity
import com.example.calmcafeapp.base.BaseFragment
import com.example.calmcafeapp.data.RecommendCafe
import com.example.calmcafeapp.databinding.FragmentRecommendedCafeBinding

class RecommendedCafeFragment : BaseFragment<FragmentRecommendedCafeBinding>(R.layout.fragment_recommended_cafe) {
    private lateinit var recommendCafeAdapter: RecommendCafeAdapter

    override fun initStartView() {
        super.initStartView()

        // 어댑터 초기화
        recommendCafeAdapter = RecommendCafeAdapter(createDummyData())

        // 리사이클러뷰 설정
        binding.recommendRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2) // 2열 그리드 레이아웃 설정
            adapter = recommendCafeAdapter
        }
    }

    override fun initDataBinding() {
        super.initDataBinding()

        // 아이템 클릭 리스너 설정
        recommendCafeAdapter.setMyItemClickListener(object : RecommendCafeAdapter.MyItemClickListener {
            override fun onItemClick(cafe: RecommendCafe) {
                val fragment = CafeDetailFragment().apply {
                    arguments = Bundle().apply {
                        putInt("cafeId", cafe.cafeId)  // 전달할 데이터 설정
                        putString("title", cafe.title)
                    }
                }
                (activity as UserActivity).addFragment(fragment)
            }
        })
    }

    override fun initAfterBinding() {
        super.initAfterBinding()
    }

    // 더미 데이터 생성 함수
    private fun createDummyData(): ArrayList<RecommendCafe> {
        return arrayListOf(
            RecommendCafe(1, "A Twosome Place", "서울특별시 00구", "현재 혼잡도: 보통", R.drawable.cafe_img1),
            RecommendCafe(2, "Moment Coffee", "서울특별시 00구", "현재 혼잡도: 혼잡", R.drawable.cafe_img2),
            RecommendCafe(3, "Hidden Leaf Cafe", "서울특별시 00구", "현재 혼잡도: 매우 혼잡",  R.drawable.cafe_img3),
            RecommendCafe(4, "Luna Coffee", "서울특별시 00구", "현재 혼잡도: 한산", R.drawable.cafe_img4),
            RecommendCafe(5, "Jane's Coffee", "서울특별시 00구", "현재 혼잡도: 보통", R.drawable.cafe_img5),
            RecommendCafe(6, "Coffee Smith", "서울특별시 00구", "현재 혼잡도: 혼잡", R.drawable.cafe_img6)
        )
    }
}
