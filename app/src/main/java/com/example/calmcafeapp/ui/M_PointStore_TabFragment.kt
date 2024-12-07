package com.example.calmcafeapp.ui

import android.util.Log
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calmcafeapp.OwnerActivity
import com.example.calmcafeapp.R
import com.example.calmcafeapp.base.BaseFragment
import com.example.calmcafeapp.data.PointDiscount
import com.example.calmcafeapp.databinding.FragmentMPointStoreTabBinding
import com.example.calmcafeapp.viewmodel.M_SettingViewModel


class M_PointStore_TabFragment : BaseFragment<FragmentMPointStoreTabBinding>(R.layout.fragment_m__point_store__tab) {

    private val viewModel: M_SettingViewModel by activityViewModels()
    private lateinit var pointStoreAdapter: PointStoreAdapter
    private var selectedPointMenu: List<PointDiscount> = emptyList()

    override fun initStartView() {
        super.initStartView()

        pointStoreAdapter = PointStoreAdapter(emptyList()) { selected ->

            Log.d("selected", "$selected")
            selectedPointMenu = selected
            binding.deleteButton.isEnabled = selectedPointMenu.isNotEmpty()
        }
        binding.rvPointmenu.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPointmenu.adapter = pointStoreAdapter

        viewModel.fetchPointDiscountMenus()

        binding.addButton.setOnClickListener {
            val fragment = PointMenuRegistrationFragment()
            (activity as OwnerActivity).addFragment(fragment)
        }
    }

    override fun initDataBinding() {
        super.initDataBinding()

        viewModel.pointDiscountListLiveData.observe(viewLifecycleOwner) { pointmenu ->
            if (pointmenu != null && pointmenu.isNotEmpty()) {
                pointStoreAdapter.updateData(pointmenu)

            } else {

            }
        }


        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        binding.deleteButton.setOnClickListener {
            if (selectedPointMenu.isEmpty()) {
                Toast.makeText(requireContext(), "삭제할 상품을 선택해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                selectedPointMenu.forEach { menu ->
                    Log.d("selected", "$menu")
                    viewModel.removePointMenu(menu.id.toLong())
                }
                Toast.makeText(requireContext(), "선택된 상품이 삭제되었습니다.", Toast.LENGTH_SHORT).show()

                // 선택된 프로모션 초기화
                selectedPointMenu = emptyList()
                binding.deleteButton.isEnabled = false

                // 프로모션 리스트 갱신
                viewModel.fetchPointDiscountMenus()
            }
        }


    }

    override fun initAfterBinding() {
        super.initAfterBinding()
    }
}
