package com.example.calmcafeapp.ui

import GridSpacingWithDividerDecoration
import android.os.Bundle
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.calmcafeapp.MainActivity
import com.example.calmcafeapp.R
import com.example.calmcafeapp.UserActivity
import com.example.calmcafeapp.base.BaseFragment
import com.example.calmcafeapp.data.RecommendCafe
import com.example.calmcafeapp.databinding.FragmentRecommendedCafeBinding
import com.example.calmcafeapp.viewmodel.RankViewModel

class RecommendedCafeFragment : BaseFragment<FragmentRecommendedCafeBinding>(R.layout.fragment_recommended_cafe) {
    private lateinit var recommendCafeAdapter: RecommendCafeAdapter
    private val rankViewModel: RankViewModel by activityViewModels()

    override fun initStartView() {
        super.initStartView()

        // 어댑터 초기화
        recommendCafeAdapter = RecommendCafeAdapter(createDummyData())

        // 리사이클러뷰 설정
        binding.recommendRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2) // 2열 그리드 레이아웃 설정
            adapter = recommendCafeAdapter

            // ItemDecoration 추가
            addItemDecoration(
                GridSpacingWithDividerDecoration(
                    spanCount = 2,
                    spacing = 15, // 각 아이템 간의 간격
                    dividerHeight = 0, // 구분선 높이
                    dividerColor = requireContext().getColor(R.color.dividerColor) // 구분선 색상
                )
            )
        }

//        // ViewModel의 recommendStoreList 관찰
//        rankViewModel.recommendStoreList.observe(viewLifecycleOwner) { recommendList ->
//            recommendCafeAdapter.updateRecommendList(recommendList)
//        }
    }

    override fun initDataBinding() {
        super.initDataBinding()

//        // ViewModel의 recommendStoreList 관찰하여 RecyclerView 업데이트
//        rankViewModel.recommendStoreList.observe(viewLifecycleOwner) { recommendList ->
//            recommendCafeAdapter.updateRecommendList(recommendList)
//        }

        /* 아이템 클릭 리스너 설정
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
        })*/
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
