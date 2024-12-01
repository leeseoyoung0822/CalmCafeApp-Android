package com.example.calmcafeapp.ui

import com.example.calmcafeapp.R
import com.example.calmcafeapp.base.BaseFragment
import com.example.calmcafeapp.databinding.FragmentMStoreBinding
import com.google.android.material.tabs.TabLayoutMediator
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class M_StoreFragment : BaseFragment<FragmentMStoreBinding>(R.layout.fragment_m__store) {

    override fun initStartView() {
        super.initStartView()

        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager

        // StorePagerAdapter를 생성하여 ViewPager에 연결
        val adapter = M_DataPagerAdapter(this)
        adapter.addFragment(M_dataVistorFragment(), "카페 방문자")
        adapter.addFragment(M_DataFavoriteFragment(), "주변 즐겨찾기 카페")
        adapter.addFragment(M_DataCongestionFragment(), "주간 혼잡도 예측")

        viewPager.adapter = adapter

        // TabLayoutMediator로 탭과 ViewPager 연결
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = adapter.getPageTitle(position)
        }.attach()
    }

    override fun initDataBinding() {
        super.initDataBinding()
    }

    override fun initAfterBinding() {
        super.initAfterBinding()
    }

    // Adapter 내부 클래스
    private inner class M_DataPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        private val fragmentList = mutableListOf<Fragment>()
        private val fragmentTitleList = mutableListOf<String>()

        // 프래그먼트와 제목 추가 메서드
        fun addFragment(fragment: Fragment, title: String) {
            fragmentList.add(fragment)
            fragmentTitleList.add(title)
        }

        override fun getItemCount(): Int = fragmentList.size

        override fun createFragment(position: Int): Fragment = fragmentList[position]

        fun getPageTitle(position: Int): String = fragmentTitleList[position]
    }
}
