package com.example.calmcafeapp.ui

import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calmcafeapp.R
import com.example.calmcafeapp.base.BaseFragment
import com.example.calmcafeapp.data.CafeCouponData
import com.example.calmcafeapp.databinding.FragmentTaphomeBinding
import com.example.calmcafeapp.viewmodel.RankViewModel

class TapHomeFragment : BaseFragment<FragmentTaphomeBinding>(R.layout.fragment_taphome) {
    private lateinit var tapHomeAdapter: TapHomeAdapter
    private lateinit var couponCafeAdapter: CouponCafeAdapter
    private val rankViewModel: RankViewModel by activityViewModels()

    override fun initStartView() {
        super.initStartView()

        /* 어댑터 초기화
        tapHomeAdapter = TapHomeAdapter(arrayListOf())*/
        couponCafeAdapter = CouponCafeAdapter(createDummyCouponData())

        // 리사이클러뷰 설정
        /*binding.menuList.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = tapHomeAdapter
        }*/
        binding.couponList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = couponCafeAdapter
        }

        /*RecyclerView 스크롤 이벤트 처리
        binding.menuList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    // 스크롤할 때 BottomSheet가 확장되도록 처리 (옵션)
                }
            }
        })*/
    }

    override fun initDataBinding() {
        super.initDataBinding()

        /*
        rankViewModel.menuList.observe(viewLifecycleOwner) { menuList ->
            tapHomeAdapter.updateMenuList(menuList)
        }*/
        rankViewModel.storeCongestionLevel.observe(viewLifecycleOwner) { storeCongestionLevel ->
            storeCongestionLevel?.let {
                updateCircularProgress(binding.ownerCircularProgressViewBoss, it)
            }
        }
        rankViewModel.userCongestionLevel.observe(viewLifecycleOwner) { userCongestionLevel ->
            userCongestionLevel?.let {
                updateCircularProgress(binding.visitorCircularProgressViewVisitor, it)
            }
        }
    }

    override fun initAfterBinding() {
        super.initAfterBinding()
    }

    // 쿠폰 더미 데이터 생성
    private fun createDummyCouponData(): ArrayList<CafeCouponData> {
        return arrayListOf(
            CafeCouponData(
                1,
                "첫방문 50% 할인",
                "50%",
                "2024-12-31"
            ),
            CafeCouponData(
                2,
                "카페라떼 1+1",
                "1+1",
                "2024-11-30"
            ),
        )
    }


    private fun updateCircularProgress(view: CircularProgressView, crowdLevel: String) {
        val (percentage, text) = when (crowdLevel) {
            "CALM" -> 30f to "한산"
            "NORMAL" -> 60f to "보통"
            "BUSY" -> 100f to "혼잡"
            else -> 0f to "정보 없음"
        }
        view.setPercentage(percentage, text)
    }

}
