package com.example.calmcafeapp.ui

import MDataViewModel
import androidx.fragment.app.activityViewModels
import com.bumptech.glide.Glide
import com.example.calmcafeapp.R
import com.example.calmcafeapp.base.BaseFragment
import com.example.calmcafeapp.databinding.FragmentMDataVistorBinding

class M_DataVistorFragment : BaseFragment<FragmentMDataVistorBinding>(R.layout.fragment_m_data_vistor) {
    private val viewModel: MDataViewModel by activityViewModels()

    override fun initStartView() {
        super.initStartView()

        // ViewModel 데이터 관찰
        viewModel.visitData.observe(viewLifecycleOwner) { data ->
            if (data != null) {
                // 이미지 로드
                Glide.with(this)
                    .load(data.ageImageUrl) // 연령별 분석 이미지 URL
                    .into(binding.imageAge)

                Glide.with(this)
                    .load(data.genderImageUrl) // 성별 분석 이미지 URL
                    .into(binding.imageGender)

                Glide.with(this)
                    .load(data.favoriteMenuDistributionImageUrl) // 성별 분석 이미지 URL
                    .into(binding.imageMenu)
            }
        }

        // 데이터 가져오기
        viewModel.fetchVisitData()
    }

    override fun initDataBinding() {
        // 데이터 바인딩 (현재는 필요 없음)
    }

    override fun initAfterBinding() {
        // 추가 설정 (현재는 필요 없음)
    }
}