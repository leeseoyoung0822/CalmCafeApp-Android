package com.example.calmcafeapp.ui

import android.app.Dialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.bumptech.glide.Glide
import com.example.calmcafeapp.R
import com.example.calmcafeapp.UserActivity
import com.example.calmcafeapp.base.BaseFragment
import com.example.calmcafeapp.data.CafeCouponData
import com.example.calmcafeapp.data.PointCoupon
import com.example.calmcafeapp.data.PointMenuDetailResDto
import com.example.calmcafeapp.databinding.DialogCouponBinding
import com.example.calmcafeapp.databinding.FragmentCouponBinding
import com.example.calmcafeapp.viewmodel.HomeViewModel
import com.example.calmcafeapp.viewmodel.SettingViewModel

class Setting_CouponFragment : BaseFragment<FragmentCouponBinding>(R.layout.fragment_coupon) {

    private val settingViewModel: SettingViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()
    private lateinit var adapter: SettingPointCouponAdapter

    override fun initStartView() {
        super.initStartView()
        (activity as UserActivity).binding.navigationUser.visibility = View.GONE
        // RecyclerView 초기화
        adapter = SettingPointCouponAdapter(emptyList()) { coupon ->
            showCouponPopup(coupon)
        }
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

    private fun showCouponPopup(pointMenu: PointCoupon) {
        val dialog = Dialog(requireContext(), R.style.TransparentDialog)
        dialog.setContentView(R.layout.dialog_coupon_setting)

        val titleTextView = dialog.findViewById<TextView>(R.id.couponTitle)
        val expiryDateTextView = dialog.findViewById<TextView>(R.id.couponExpiryDate)
        val storeName = dialog.findViewById<TextView>(R.id.storeName)


        // 포인트 메뉴 정보 설정
        titleTextView.text = " ${pointMenu.menuName} ${pointMenu.discount}%"
        storeName.text = "${pointMenu.storeName}"
        expiryDateTextView.text = "사용기한 : ${pointMenu.expirationStart} ~ ${pointMenu.expirationEnd} "


        dialog.show()
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
