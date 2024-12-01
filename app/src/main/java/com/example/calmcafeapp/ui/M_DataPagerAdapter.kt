package com.example.calmcafeapp.ui

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter

class M_DataPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun getItemCount(): Int = 3 // 총 3개의 탭

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> M_dataVistorFragment() // 첫 번째 탭
            1 -> M_DataFavoriteFragment() // 두 번째 탭
            2 -> M_DataCongestionFragment() // 세 번째 탭
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}