package com.example.calmcafeapp.ui

import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calmcafeapp.R
import com.example.calmcafeapp.UserActivity
import com.example.calmcafeapp.base.BaseFragment
import com.example.calmcafeapp.databinding.FragmentFavoriteBinding
import com.example.calmcafeapp.viewmodel.SettingViewModel

class FavoriteFragment : BaseFragment<FragmentFavoriteBinding>(R.layout.fragment_favorite) {

    private val settingViewModel: SettingViewModel by activityViewModels()

    override fun initStartView() {

        val adapter = Setting_favoriteCafeAdapter(mutableListOf()) { storeId ->
            settingViewModel.removeFavorite(storeId)
        }
        (activity as UserActivity).binding.navigationUser.visibility = View.GONE


        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = adapter

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

    override fun onDestroyView() {
        super.onDestroyView()
    }
}
