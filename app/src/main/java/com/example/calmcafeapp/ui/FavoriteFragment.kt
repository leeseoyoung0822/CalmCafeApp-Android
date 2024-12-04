package com.example.calmcafeapp.ui

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calmcafeapp.R
import com.example.calmcafeapp.base.BaseFragment
import com.example.calmcafeapp.data.OnRouteStartListener
import com.example.calmcafeapp.databinding.FragmentFavoriteBinding
import com.example.calmcafeapp.viewmodel.HomeViewModel
import com.example.calmcafeapp.viewmodel.SettingViewModel

class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>(R.layout.fragment_favorite),OnRouteStartListener {

    private val settingViewModel: SettingViewModel by activityViewModels()
    private val homeViewModel: HomeViewModel by activityViewModels()

    override fun initStartView() {

        val adapter = Setting_favoriteCafeAdapter(mutableListOf()) { storeId ->
            settingViewModel.removeFavorite(storeId)
        }

        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

        // 아이템 클릭 시 `CafeDetailFragment` 열기
        adapter.setOnItemClickListener{ storeId ->
            showCafeDetailFragment(storeId)
        }

        // ViewModel로부터 즐겨찾기 데이터 관찰
        settingViewModel.favoriteStores.observe(viewLifecycleOwner) { stores ->
            if (stores != null) {
                adapter.updateData(stores)
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

        // 즐겨찾기 취소 후 처리
        settingViewModel.favoriteStoreId.observe(viewLifecycleOwner) { storeId ->
            if (storeId != null) {
                Toast.makeText(requireContext(), "즐겨찾기가 취소되었습니다.", Toast.LENGTH_SHORT).show()
                settingViewModel.fetchFavoriteStores() // 갱신
            }
        }
        settingViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(requireContext(), it, Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun showCafeDetailFragment(storeId: Int) {
        // 현재 위치를 설정 (임시 좌표 사용)
        val userLatitude = 37.650579
        val userLongitude = 126.867010

        // API를 통해 카페 상세 정보 가져오기
        homeViewModel.fetchCafeDetailInfo(storeId, userLatitude, userLongitude)
        Log.d("FavoriteFragment", "Opening CafeDetailFragment with storeId: $storeId")

        // CafeDetailFragment 열기
        val cafeDetailFragment = CafeDetailFragment().apply {
            setTargetFragment(this@FavoriteFragment, 0)
            arguments = Bundle().apply {
                putString("visibility", "gone")
            }
        }
        Log.d("FavoriteFragment", "CafeDetailFragment 열기: storeId=$storeId")
        cafeDetailFragment.show(parentFragmentManager, "CafeDetailFragment")
    }

    override fun onRouteStart() {
        Toast.makeText(requireContext(), "즐겨찾기에서는 길 찾기 불가", Toast.LENGTH_SHORT).show()
    }

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
