package com.example.calmcafeapp.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import com.example.calmcafeapp.R
import com.example.calmcafeapp.base.BaseFragment
import com.example.calmcafeapp.databinding.FragmentMHomeUpdateBinding
import com.example.calmcafeapp.viewmodel.M_HomeViewModel


class M_Home_UpdateFragment : BaseFragment<FragmentMHomeUpdateBinding>(R.layout.fragment_m__home__update) {

    private val mHomeViewModel: M_HomeViewModel by activityViewModels()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

    }

    override fun initStartView() {
        super.initStartView()

        setupNumberPickers()
        observeViewModel()
    }

    override fun initDataBinding() {
        super.initDataBinding()
        // 데이터 바인딩 관련 초기화 작업
    }
    override fun initAfterBinding() {
        super.initAfterBinding()

        // 뒤로가기 버튼 설정
        binding.backButton.setOnClickListener {
            parentFragmentManager.popBackStack()
        }
    }

    private fun setupNumberPickers() {
        // NumberPicker 설정
        with(binding) {
            npOpenHour.minValue = 0
            npOpenHour.maxValue = 23
            npOpenMinute.minValue = 0
            npOpenMinute.maxValue = 59

            npCloseHour.minValue = 0
            npCloseHour.maxValue = 23
            npCloseMinute.minValue = 0
            npCloseMinute.maxValue = 59

            npSeatNum.minValue = 1
            npSeatNum.maxValue = 100

            // 운영 시간 수정 버튼 클릭
            btnUpdateOpenHours.setOnClickListener {
                val openingTime = "${npOpenHour.value}:${String.format("%02d", npOpenMinute.value)}"
                val closingTime = "${npCloseHour.value}:${String.format("%02d", npCloseMinute.value)}"
                mHomeViewModel.modifyStoreHours(openingTime, closingTime)
            }

            // 라스트 오더 시간 수정 버튼 클릭
            btnUpdateLastOrder.setOnClickListener {
                val lastOrderTime = "${npCloseHour.value}:${String.format("%02d", npCloseMinute.value)}"
                mHomeViewModel.modifyLastOrderTime(lastOrderTime)
            }

            // 최대 수용 인원 수정 버튼 클릭
            btnSeatNum.setOnClickListener {
                val maxCapacity = npSeatNum.value
                mHomeViewModel.modifyMaxCapacity(maxCapacity)
            }
        }
    }

    private fun observeViewModel() {
        // 에러 메시지 관찰
        mHomeViewModel.apply {
            // 에러 메시지 관찰
            errorMessage.observe(viewLifecycleOwner) { errorMessage ->
                errorMessage?.let {
                    showToast(it)
                }
            }

            // 업데이트 성공 여부 관찰
            isUpdateSuccess.observe(viewLifecycleOwner) { isSuccess ->
                if (isSuccess) {
                    showToast("업데이트가 성공적으로 완료되었습니다.")
                } else {
                    showToast("업데이트에 실패했습니다.")
                }
            }

            // 로딩 상태 관찰
            isLoading.observe(viewLifecycleOwner) { isLoading ->
                if (isLoading) {
                    Log.d("M_Home_UpdateFragment", "처리 중...")
                }
            }
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }
    override fun onDestroyView() {
        super.onDestroyView()
        // BaseFragment에서 binding 해제 자동 처리
    }
}