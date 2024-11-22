package com.example.calmcafeapp.ui

import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calmcafeapp.R
import com.example.calmcafeapp.UserActivity
import com.example.calmcafeapp.base.BaseFragment
import com.example.calmcafeapp.data.CafeData
import com.example.calmcafeapp.databinding.FragmentFavoriteBinding
import com.example.calmcafeapp.viewmodel.SettingViewModel

class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>(R.layout.fragment_favorite) {

    private val settingViewModel: SettingViewModel by activityViewModels()

    override fun initStartView() {
        // RecyclerView 초기화

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        val adapter = Setting_favoriteCafeAdapter(mutableListOf())
        binding.recyclerView.adapter = adapter

        // ViewModel로부터 즐겨찾기 데이터 관찰
        settingViewModel.favoriteStores.observe(viewLifecycleOwner) { stores ->
            if (stores != null) {
                adapter.updateData(stores.map { store ->
                    CafeData(
                        R.drawable.coupon_img, // 임의 이미지, 필요시 API 응답에 맞게 수정
                        store.name,
                        "혼잡도: ${store.storeCongestionLevel}" // 혼잡도 정보 표시
                    )
                })
            }
        }
        // API 호출
        settingViewModel.fetchFavoriteStores()

    }

    override fun initAfterBinding() {
        // 뒤로가기 버튼 설정
        binding.backButton.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
