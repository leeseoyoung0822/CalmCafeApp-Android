package com.example.calmcafeapp.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.activityViewModels
import com.example.calmcafeapp.R
import com.example.calmcafeapp.base.BaseFragment
import com.example.calmcafeapp.databinding.FragmentPromotionRegistrationBinding
import com.example.calmcafeapp.viewmodel.M_SettingViewModel

class PromotionRegistrationFragment : BaseFragment<FragmentPromotionRegistrationBinding>(R.layout.fragment_promotion_registration) {

    private var discountRate = 10
    private var startTimeHour = 8
    private var startTimeMinute = 0
    private var endTimeHour = 22
    private var endTimeMinute = 0
    private val viewModel: M_SettingViewModel by activityViewModels()

    override fun initStartView() {
        super.initStartView()

        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        // 할인율 설정
        binding.tvDiscount.setOnClickListener {
            val discountBottomSheet = DiscountBottomSheet(discountRate) { selectedDiscount ->
                discountRate = selectedDiscount
                binding.tvDiscount.text = "${selectedDiscount}%"
            }
            discountBottomSheet.show(parentFragmentManager, "DiscountBottomSheet")
        }

        // 사용 가능 시간 설정
        binding.tvAvailableTime.setOnClickListener {
            val timeRangePickerBottomSheet = TimeRangePickerBottomSheet(
                initialStartHour = startTimeHour,
                initialStartMinute = startTimeMinute,
                initialEndHour = endTimeHour,
                initialEndMinute = endTimeMinute
            ) { selectedStartHour, selectedStartMinute, selectedEndHour, selectedEndMinute ->
                startTimeHour = selectedStartHour
                startTimeMinute = selectedStartMinute
                endTimeHour = selectedEndHour
                endTimeMinute = selectedEndMinute
                binding.tvAvailableTime.text = String.format(
                    "%02d:%02d - %02d:%02d",
                    selectedStartHour, selectedStartMinute, selectedEndHour, selectedEndMinute
                )
            }
            timeRangePickerBottomSheet.show(parentFragmentManager, "TimeRangePickerBottomSheet")
        }

        // 등록 버튼 클릭 이벤트
        binding.btnaddPromotion.setOnClickListener {
            registerPromotion(
                discount = discountRate,
                startTime = String.format("%02d:%02d", startTimeHour, startTimeMinute),
                endTime = String.format("%02d:%02d", endTimeHour, endTimeMinute),
                promotionTypeValue = 0 // Take-out 프로모션 타입
            )
        }

        binding.btnaddPromotion2.setOnClickListener {
            registerPromotion(
                discount = discountRate,
                startTime = String.format("%02d:%02d", startTimeHour, startTimeMinute),
                endTime = String.format("%02d:%02d", endTimeHour, endTimeMinute),
                promotionTypeValue = 1 // 매장 이용 프로모션 타입
            )
        }
    }

    private fun registerPromotion(discount: Int, startTime: String, endTime: String, promotionTypeValue: Int) {
        viewModel.registerPromotion(discount, startTime, endTime, promotionTypeValue)
            .also {
                viewModel.promotionRegisterResult.observe(viewLifecycleOwner) { success ->
                    if (success == true) {
                        // 프로모션 등록 성공 시 이전 화면으로 이동
                        parentFragmentManager.popBackStack()
                    } else {
                        Log.e("PromotionRegister", "Failed to register promotion")
                    }
                }
            }
    }
}
