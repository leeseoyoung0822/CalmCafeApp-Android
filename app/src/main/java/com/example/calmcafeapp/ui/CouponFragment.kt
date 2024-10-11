package com.example.calmcafeapp.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calmcafeapp.databinding.FragmentCouponBinding

class CouponFragment : Fragment() {

    private var _binding: FragmentCouponBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCouponBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // RecyclerView의 layoutManager 설정
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())

        // 샘플 데이터 추가
        val couponList = mutableListOf(
            Coupon("스타벅스", "take-out 10% 할인쿠폰", "24.10.20", "24.10.22"),
            Coupon("스타벅스", "take-out 10% 할인쿠폰", "24.10.20", "24.10.22"),
            Coupon("스타벅스", "take-out 10% 할인쿠폰", "24.10.20", "24.10.22"),
            Coupon("스타벅스", "take-out 10% 할인쿠폰", "24.10.20", "24.10.22"),
            Coupon("스타벅스", "take-out 10% 할인쿠폰", "24.10.20", "24.10.22"),
            Coupon("스타벅스", "take-out 10% 할인쿠폰", "24.10.20", "24.10.22"),
            Coupon("스타벅스", "take-out 10% 할인쿠폰", "24.10.20", "24.10.22"),
            Coupon("스타벅스", "take-out 10% 할인쿠폰", "24.10.20", "24.10.22")
        )

        val couponAdapter = CouponAdapter(couponList)
        binding.recyclerView.adapter = couponAdapter

        binding.backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

