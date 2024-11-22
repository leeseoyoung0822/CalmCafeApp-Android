package com.example.calmcafeapp.ui

import android.os.Bundle
import com.example.calmcafeapp.R
import com.example.calmcafeapp.base.BaseFragment
import com.example.calmcafeapp.databinding.FragmentMSettingBinding
import com.google.android.material.tabs.TabLayoutMediator
import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2

class M_SettingFragment : BaseFragment<FragmentMSettingBinding>(R.layout.fragment_m__setting) {

    override fun initStartView() {
        super.initStartView()

        val tabLayout = binding.tabLayout
        val viewPager = binding.viewPager

        // Initialize the adapter and add fragments for each tab
        val adapter = SettingsPagerAdapter(this)
        adapter.addFragment(M_Menu_TabFragment(), "메뉴 관리")
        adapter.addFragment(M_Promotion_TabFragment(), "프로모션")
        adapter.addFragment(M_PointStore_TabFragment(), "포인트 스토어")

        viewPager.adapter = adapter

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

    private inner class SettingsPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

        private val fragmentList = mutableListOf<Fragment>()
        private val fragmentTitleList = mutableListOf<String>()

        fun addFragment(fragment: Fragment, title: String) {
            fragmentList.add(fragment)
            fragmentTitleList.add(title)
        }

        override fun getItemCount(): Int = fragmentList.size

        override fun createFragment(position: Int): Fragment = fragmentList[position]

        fun getPageTitle(position: Int): String = fragmentTitleList[position]
    }
}

