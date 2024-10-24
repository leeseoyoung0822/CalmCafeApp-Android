package com.example.calmcafeapp.ui

import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calmcafeapp.R
import com.example.calmcafeapp.base.BaseFragment
import com.example.calmcafeapp.data.CafeCouponData
import com.example.calmcafeapp.databinding.FragmentCouponBinding

class CouponFragment : BaseFragment<FragmentCouponBinding>(R.layout.fragment_coupon) {

    override fun initStartView() {
        // RecyclerView의 layoutManager 설정
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 샘플 데이터 추가
        val couponList: ArrayList<CafeCouponData> = arrayListOf(
            CafeCouponData(1, "take-out 10% 할인쿠폰", "24.10.20", "24.10.22"),
            CafeCouponData(2, "take-out 10% 할인쿠폰", "24.10.20", "24.10.22"),
            CafeCouponData(3, "take-out 10% 할인쿠폰", "24.10.20", "24.10.22"),
            CafeCouponData(4, "take-out 10% 할인쿠폰", "24.10.20", "24.10.22"),
            CafeCouponData(5, "take-out 10% 할인쿠폰", "24.10.20", "24.10.22"),
            CafeCouponData(6, "take-out 10% 할인쿠폰", "24.10.20", "24.10.22")
        )

        val couponAdapter = CouponCafeAdapter(couponList)
        binding.recyclerView.adapter = couponAdapter
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
