package com.example.calmcafeapp.ui

import GridSpacingWithDividerDecoration
import RecommendCafeAdapter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import com.example.calmcafeapp.R
import com.example.calmcafeapp.base.BaseFragment
import com.example.calmcafeapp.data.OnRouteStartListener
import com.example.calmcafeapp.data.RecommendStoreResDto
import com.example.calmcafeapp.databinding.FragmentRecommendedCafeBinding
import com.example.calmcafeapp.viewmodel.HomeViewModel

class RecommendedCafeFragment : BaseFragment<FragmentRecommendedCafeBinding>(R.layout.fragment_recommended_cafe),OnRouteStartListener {

    private lateinit var recommendCafeAdapter: RecommendCafeAdapter
    private val viewModel: HomeViewModel by activityViewModels()

    override fun initStartView() {
        super.initStartView()

        // 어댑터 초기화
        recommendCafeAdapter = RecommendCafeAdapter(arrayListOf())
        recommendCafeAdapter.setMyItemClickListener(object : RecommendCafeAdapter.MyItemClickListener {
            override fun onItemClick(cafe: RecommendStoreResDto) {
                showCafeDetailFragment(cafe.id)
                // 아이템 클릭 이벤트 처리
            }
        })

        // RecyclerView 설정
        binding.recommendRecyclerView.apply {
            layoutManager = GridLayoutManager(requireContext(), 2) // 2열 그리드 레이아웃
            adapter = recommendCafeAdapter

            // ItemDecoration 추가 (간격 설정)
            addItemDecoration(
                GridSpacingWithDividerDecoration(
                    spanCount = 2,
                    spacing = 15,
                    dividerHeight = 0,
                    dividerColor = requireContext().getColor(R.color.dividerColor)
                )
            )
        }

        // ViewModel 관찰
        viewModel.recommendCafeList.observe(viewLifecycleOwner) { cafeList ->
            cafeList?.let {
                recommendCafeAdapter.updateData(it)
            }
        }
    }

    private fun showCafeDetailFragment(storeId: Int) {
        val userLatitude = 37.5665 // 임시 값
        val userLongitude = 126.9780 // 임시 값

        // API 호출
        viewModel.fetchCafeDetailInfo(storeId, userLatitude, userLongitude)
        Log.d("RecommendedCafeFragment", "Opening CafeDetailFragment with storeId: $storeId")

        // CafeDetailFragment 열기
        val cafeDetailFragment = CafeDetailFragment().apply {
            setTargetFragment(this@RecommendedCafeFragment, 0)
            arguments = Bundle().apply {
                putString("visibility", "gone")
            }
        }
        cafeDetailFragment.show(parentFragmentManager, "CafeDetailFragment")
    }

    override fun onRouteStart() {
        Toast.makeText(requireContext(), "추천카페에서는 길 찾기 불가", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }


}
