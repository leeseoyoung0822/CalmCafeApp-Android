package com.example.calmcafeapp.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calmcafeapp.OwnerActivity
import com.example.calmcafeapp.R
import com.example.calmcafeapp.base.BaseFragment
import com.example.calmcafeapp.data.PointDiscount
import com.example.calmcafeapp.data.Promotion
import com.example.calmcafeapp.databinding.FragmentMPointStoreTabBinding
import com.example.calmcafeapp.databinding.FragmentMPromotionTabBinding
import com.example.calmcafeapp.viewmodel.M_SettingViewModel


class M_PointStore_TabFragment : BaseFragment<FragmentMPointStoreTabBinding>(R.layout.fragment_m__point_store__tab) {

    private val viewModel: M_SettingViewModel by activityViewModels()
    private lateinit var pointStoreAdapter: PointStoreAdapter
    private var selectedPointMenu: List<PointDiscount> = emptyList()

    override fun initStartView() {
        super.initStartView()

        pointStoreAdapter = PointStoreAdapter(emptyList()) { selected ->
            selectedPointMenu = selected
            binding.deleteButton.isEnabled = selectedPointMenu.isNotEmpty()
        }
        binding.rvPointmenu.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPointmenu.adapter = pointStoreAdapter

        viewModel.fetchPointDiscountMenus()
    }

    override fun initDataBinding() {
        super.initDataBinding()


        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

//        // 등록 버튼 클릭 시


    }

    override fun initAfterBinding() {
        super.initAfterBinding()
    }
}
