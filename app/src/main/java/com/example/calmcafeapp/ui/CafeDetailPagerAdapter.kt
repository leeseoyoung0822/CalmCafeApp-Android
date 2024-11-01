package com.example.calmcafeapp.ui

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter

class CafeDetailPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TapHomeFragment()   // 홈
            1 -> RecommendedCafeFragment()  // 추천카페
            2 -> PointMenuFragment()        // 스토어
            else -> throw IllegalStateException("Unexpected position $position")
        }
    }
}
