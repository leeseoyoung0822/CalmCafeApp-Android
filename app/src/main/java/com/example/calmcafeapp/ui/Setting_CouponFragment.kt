package com.example.calmcafeapp.ui

import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calmcafeapp.R
import com.example.calmcafeapp.UserActivity
import com.example.calmcafeapp.base.BaseFragment
import com.example.calmcafeapp.data.CafeCouponData
import com.example.calmcafeapp.databinding.FragmentCouponBinding
import com.example.calmcafeapp.viewmodel.SettingViewModel

class Setting_CouponFragment : BaseFragment<FragmentCouponBinding>(R.layout.fragment_coupon) {

    private val settingViewModel: SettingViewModel by activityViewModels()

    override fun initStartView() {
        // RecyclerView초기화
        val adapter = SettingPointCouponAdapter(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        settingViewModel.pointCoupons.observe(viewLifecycleOwner) { coupons ->
            adapter.updateData(coupons ?: emptyList())
        }
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
