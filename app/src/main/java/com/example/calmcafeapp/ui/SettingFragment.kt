package com.example.calmcafeapp.ui

import android.os.Bundle
import com.example.calmcafeapp.R
import com.example.calmcafeapp.base.BaseFragment
import com.example.calmcafeapp.databinding.FragmentSettingBinding


class SettingFragment : BaseFragment<FragmentSettingBinding>(R.layout.fragment_setting) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        // 필요한 초기화 코드가 있다면 여기에 작성
    }

    override fun initStartView() {
        super.initStartView()
        // 시작 뷰 초기화 작업 (ex. 리사이클러뷰 초기화)
    }

    override fun initDataBinding() {
        super.initDataBinding()
        // 데이터 바인딩 설정을 여기에 구현
    }

    override fun initAfterBinding() {
        super.initAfterBinding()

        // 즐겨찾기 섹션 클릭 시 FavoriteFragment로 이동
        binding.favoritesSection.setOnClickListener {
            val favoriteFragment = FavoriteFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, favoriteFragment)
                .addToBackStack(null)
                .commit()
        }

        // 쿠폰 섹션 클릭 시 CouponFragment로 이동
        binding.mycoupon.setOnClickListener {
            val couponFragment = CouponFragment()
            parentFragmentManager.beginTransaction()
                .replace(R.id.fragment_container, couponFragment)
                .addToBackStack(null)
                .commit()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        // BaseFragment에서는 binding 해제를 처리하지 않아도 됨
    }
}

