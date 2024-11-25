package com.example.calmcafeapp.ui

import android.util.Log
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
    private lateinit var adapter: SettingPointCouponAdapter

    override fun initStartView() {
        super.initStartView()

        // RecyclerView 초기화
        adapter = SettingPointCouponAdapter(emptyList())
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        // API 호출하여 쿠폰 데이터 가져오기
        settingViewModel.fetchPointCoupons()

        // ViewModel 데이터 관찰
        observeViewModel()

    }

    private fun observeViewModel() {
        settingViewModel.pointCoupons.observe(viewLifecycleOwner) { coupons ->
            if (coupons.isNullOrEmpty()) {
                Log.d("Setting_CouponFragment", "No coupons available")
                binding.noCouponText.visibility = View.VISIBLE // 쿠폰 없음 표시
                binding.recyclerView.visibility = View.GONE
            } else {
                Log.d("Setting_CouponFragment", "Coupons fetched: $coupons")
                binding.noCouponText.visibility = View.GONE
                binding.recyclerView.visibility = View.VISIBLE
                adapter.updateData(coupons)
            }
        }

        settingViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Log.e("Setting_CouponFragment", "Error fetching coupons: $it")
                binding.noCouponText.text = "쿠폰을 가져오는 데 실패했습니다."
                binding.noCouponText.visibility = View.VISIBLE
                binding.recyclerView.visibility = View.GONE
            }
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
