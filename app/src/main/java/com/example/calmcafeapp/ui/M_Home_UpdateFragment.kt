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
        with(binding) {
            // 영업 시작 시간
            npOpenHour.apply {
                minValue = 0
                maxValue = 23
                setFormatter { it.toTwoDigitString() }
            }
            npOpenMinute.apply {
                minValue = 0
                maxValue = 59
                setFormatter { it.toTwoDigitString() }
            }

            // 영업 종료 시간
            npCloseHour.apply {
                minValue = 0
                maxValue = 23
                setFormatter { it.toTwoDigitString() }
            }
            npCloseMinute.apply {
                minValue = 0
                maxValue = 59
                setFormatter { it.toTwoDigitString() }
            }

            // 라스트 오더 시간
            npLastOrderHour.apply {
                minValue = 0
                maxValue = 23
                setFormatter { it.toTwoDigitString() }
            }
            npLastOrderMinute.apply {
                minValue = 0
                maxValue = 59
                setFormatter { it.toTwoDigitString() }
            }

            // 최대 좌석 수
            npSeatNum.apply {
                minValue = 1
                maxValue = 100
            }

            // HomeFragment에서 전달받은 값을 초기값으로 설정
            val cafeDetail = mHomeViewModel.cafeDetail.value?.result
            if (cafeDetail != null) {
                val (openHour, openMinute) = cafeDetail.openingTime.split(":").map { it.toInt() }
                val (closeHour, closeMinute) = cafeDetail.closingTime.split(":").map { it.toInt() }
                val (lastOrderHour, lastOrderMinute) = cafeDetail.lastOrderTime.split(":").map { it.toInt() }

                npOpenHour.value = openHour
                npOpenMinute.value = openMinute
                npCloseHour.value = closeHour
                npCloseMinute.value = closeMinute
                npLastOrderHour.value = lastOrderHour
                npLastOrderMinute.value = lastOrderMinute
                npSeatNum.value = cafeDetail.maxCustomerCount
            } else {
                // 초기값 없을 때 기본값 설정
                npOpenHour.value = 9
                npOpenMinute.value = 0
                npCloseHour.value = 18
                npCloseMinute.value = 0
                npLastOrderHour.value = 17
                npLastOrderMinute.value = 30
                npSeatNum.value = 10
            }


            // 운영 시간 수정 버튼 클릭
            btnUpdateOpenHours.setOnClickListener {
                val openingTime = "${npOpenHour.value.toTwoDigitString()}:${npOpenMinute.value.toTwoDigitString()}:00"
                val closingTime = "${npCloseHour.value.toTwoDigitString()}:${npCloseMinute.value.toTwoDigitString()}:00"
                mHomeViewModel.modifyStoreHours(openingTime, closingTime)
            }

            // 라스트 오더 시간 수정 버튼 클릭
            btnUpdateLastOrder.setOnClickListener {
                val lastOrderTime = "${npLastOrderHour.value.toTwoDigitString()}:${npLastOrderMinute.value.toTwoDigitString()}:00"
                mHomeViewModel.modifyLastOrderTime(lastOrderTime)
            }

            // 최대 수용 인원 수정 버튼 클릭
            btnSeatNum.setOnClickListener {
                val maxCapacity = npSeatNum.value
                Log.d("API Request", "Max Capacity: $maxCapacity")
                mHomeViewModel.modifyMaxCapacity(maxCapacity)
            }
        }
    }

    // 확장 함수
    fun Int.toTwoDigitString(): String {
        return String.format("%02d", this)
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