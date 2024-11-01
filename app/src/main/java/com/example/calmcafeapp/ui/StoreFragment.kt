package com.example.calmcafeapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.calmcafeapp.R
import com.example.calmcafeapp.base.BaseFragment
import com.example.calmcafeapp.data.CafeMenuData
import com.example.calmcafeapp.data.PointMenuDetailResDto
import com.example.calmcafeapp.databinding.FragmentStoreBinding
import com.example.calmcafeapp.databinding.FragmentTaphomeBinding

class StoreFragment : BaseFragment<FragmentStoreBinding>(R.layout.fragment_store) {
    private lateinit var menuPointStoreAdapter: MenuPointStoreAdapter

    override fun initStartView() {
        super.initStartView()
        menuPointStoreAdapter = MenuPointStoreAdapter(createDummyData())
        binding.rvPointmenu.apply {
            layoutManager = GridLayoutManager(requireContext(), 2) // 수정됨: Context 전달
            adapter = menuPointStoreAdapter // 수정됨: Adapter 설정
        }

    }

    override fun initDataBinding() {
        super.initDataBinding()
    }

    override fun initAfterBinding() {
        super.initAfterBinding()
    }

    private fun createDummyData(): ArrayList<PointMenuDetailResDto> {
        return arrayListOf(
            PointMenuDetailResDto(
                1,
                "아이스 아메리카노",
                4000,
                10,
                "R.drawable.coffee"
            ),
            PointMenuDetailResDto(
                2,
                "카페라떼",
                4500,
                10,
                "R.drawable.coffee"
            ),
            PointMenuDetailResDto(
                3,
                "바닐라 라떼",
                5000,
                10,
                "R.drawable.coffee"
            ),
            PointMenuDetailResDto(
                4,
                "콜드브루",
                5000,
                5,
                "R.drawable.coffee"
            ),
            PointMenuDetailResDto(
                5,
                "플랫 화이트",
                4500,
                15,
                "R.drawable.coffee"
            ),
            PointMenuDetailResDto(
                6,
                "카라멜 마끼아또",
                5500,
                50,
                "R.drawable.coffee"
            )
        )
    }
}