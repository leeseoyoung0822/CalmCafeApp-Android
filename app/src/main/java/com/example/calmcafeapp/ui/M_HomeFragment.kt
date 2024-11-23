package com.example.calmcafeapp.ui

import android.util.Log
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.calmcafeapp.R
import com.example.calmcafeapp.base.BaseFragment
import com.example.calmcafeapp.data.M_CafeDetailResult
import com.example.calmcafeapp.data.TimeData
import com.example.calmcafeapp.databinding.FragmentMHomeBinding
import com.example.calmcafeapp.viewmodel.M_HomeViewModel

class M_HomeFragment : BaseFragment<FragmentMHomeBinding>(R.layout.fragment_m__home) {

    private val mHomeViewModel: M_HomeViewModel by activityViewModels()

    override fun initStartView() {
        super.initStartView()

        // ViewModel에서 데이터 가져오기
        mHomeViewModel.fetchCafeDetail()

        // ViewModel 데이터 관찰
        observeViewModel()

        // 수정 버튼 클릭 이벤트 설정
        binding.updateBtn.setOnClickListener {
            navigateToUpdateFragment()
        }
    }

    private fun navigateToUpdateFragment() {
        parentFragmentManager.beginTransaction()
            .replace(R.id.container_owner, M_Home_UpdateFragment()) // UpdateFragment로 이동
            .addToBackStack(null) // 뒤로 가기 가능하도록 설정
            .commit()
    }

    private fun observeViewModel() {
        // 카페 상세 정보 관찰
        mHomeViewModel.cafeDetail.observe(viewLifecycleOwner) { response ->
            response?.let {
                if (it.isSuccess) {
                    updateUI(it.result)
                } else {
                    Log.e("M_HomeFragment", "카페 상세 정보 조회 실패: ${it.message}")
                }
            }
        }


        // 에러 메시지 관찰
        mHomeViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Log.e("M_HomeFragment", "Error: $it")
            }
        }
    }

    private fun updateUI(data: M_CafeDetailResult) {
        binding.apply {
            // 가게 이름
            storeName.text = data.storeName

            // 가게 주소
            storeAddress.text = data.address

            // 가게 이미지
            Glide.with(this@M_HomeFragment)
                .load(data.image)
                .placeholder(R.drawable.rounded_background)
                .error(R.drawable.logo)
                .into(imagePlaceholder)

            // 혼잡도
            // 혼잡도 표시 (사장님 및 방문자)
            updateCircularProgress(userCircularProgressViewBoss, data.userCongestionLevel)
            updateCircularProgress(aiCircularProgressViewVisitor, data.storeCongestionLevel)


            // 운영 시간 (분 단위까지만 표시)
            val formattedOpeningTime = data.openingTime.substringBeforeLast(":")
            val formattedClosingTime = data.closingTime.substringBeforeLast(":")
            time.text = "운영 시간: $formattedOpeningTime ~ $formattedClosingTime"

            // 라스트 오더 (분 단위까지만 표시)
            val formattedLastOrderTime = data.lastOrderTime.substringBeforeLast(":")
            lastOrder.text = "라스트 오더: $formattedLastOrderTime 까지"

            // 좌석 정보 (최대 수용 인원)
            seatNum.text = "최대 수용 인원: ${data.maxCustomerCount}명"
        }
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
