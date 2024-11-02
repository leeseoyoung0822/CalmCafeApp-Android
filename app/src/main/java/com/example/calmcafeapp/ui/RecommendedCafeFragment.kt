package com.example.calmcafeapp.ui

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
        recommendCafeAdapter = RecommendCafeAdapter(arrayListOf())

        // 리사이클러뷰 설정
        binding.recommendRecyclerView.apply {
            layoutManager = GridLayoutManager(context, 2) // 2열 그리드 레이아웃 설정
            adapter = recommendCafeAdapter
        }

        // ViewModel의 recommendStoreList 관찰
        rankViewModel.recommendStoreList.observe(viewLifecycleOwner) { recommendList ->
            recommendCafeAdapter.updateRecommendList(recommendList)
        }
    }

    override fun initDataBinding() {
        super.initDataBinding()

        // ViewModel의 recommendStoreList 관찰하여 RecyclerView 업데이트
        rankViewModel.recommendStoreList.observe(viewLifecycleOwner) { recommendList ->
            recommendCafeAdapter.updateRecommendList(recommendList)
        }

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
}
