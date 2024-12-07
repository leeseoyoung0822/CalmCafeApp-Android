package com.example.calmcafeapp.ui

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calmcafeapp.R
import com.example.calmcafeapp.base.BaseFragment
import com.example.calmcafeapp.data.PointDiscount
import com.example.calmcafeapp.databinding.FragmentPointMenuRegistrationBinding
import com.example.calmcafeapp.viewmodel.M_SettingViewModel

class PointMenuRegistrationFragment : BaseFragment<FragmentPointMenuRegistrationBinding>(R.layout.fragment_point_menu_registration) {

    private var discountRate = 10
    private var startTimeHour = 8
    private var startTimeMinute = 0
    private var endTimeHour = 22
    private var endTimeMinute = 0
    private val viewModel: M_SettingViewModel by activityViewModels()
    private lateinit var pointStoreMenuAdapter: PointStoreMenuAdapter
    private lateinit var pointStoreNonMenuAdapter: PointStoreNonMenuAdapter

    @SuppressLint("DefaultLocale")
    override fun initStartView() {
        super.initStartView()

        binding.btnBack.setOnClickListener {
            parentFragmentManager.popBackStack()
        }

        pointStoreMenuAdapter = PointStoreMenuAdapter(
            menus = emptyList(),
            onEditClick = { selectedMenu ->
                viewModel.registerMenuPointStore(selectedMenu.id.toLong(), selectedMenu.pointDiscount, 5000)
                viewModel.fetchRegPointDiscountMenus()
                Toast.makeText(requireContext(), "${selectedMenu.name} 수정 완료", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            },
            onDiscountClick = { selectedMenu ->
                showDiscountBottomSheet(selectedMenu)
            }
        )
        binding.rvMenu.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = pointStoreMenuAdapter
        }
        viewModel.fetchRegPointDiscountMenus()

        pointStoreNonMenuAdapter = PointStoreNonMenuAdapter(
            menus = emptyList(),
            onEditClick = { selectedMenu ->
                viewModel.registerMenuPointStore(selectedMenu.id.toLong(), selectedMenu.pointDiscount, 5000)
                viewModel.fetchRegPointDiscountNonMenus()
                Toast.makeText(requireContext(), "${selectedMenu.name} 등록 완료", Toast.LENGTH_SHORT).show()
                parentFragmentManager.popBackStack()
            },
            onDiscountClick = { selectedMenu ->
                shownonDiscountBottomSheet(selectedMenu)
            }
        )

        binding.rvNonmenu.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = pointStoreNonMenuAdapter
        }

        viewModel.fetchRegPointDiscountNonMenus()
    }

    override fun initDataBinding() {
        super.initDataBinding()

        viewModel.pointDiscountMenuListLiveData.observe(viewLifecycleOwner) { pointmenu ->
            if (pointmenu != null && pointmenu.isNotEmpty()) {
                pointStoreMenuAdapter.updateMenus(pointmenu)
            } else {
            }
        }

        viewModel.pointDiscountNonMenuListLiveData.observe(viewLifecycleOwner) { pointmenu ->
            if (pointmenu != null && pointmenu.isNotEmpty()) {
                pointStoreNonMenuAdapter.updateMenus(pointmenu)
            } else {
            }
        }


        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun showDiscountBottomSheet(menu: PointDiscount) {
        val discountBottomSheet = DiscountBottomSheet(menu.pointDiscount) { selectedDiscount ->
            menu.pointDiscount = selectedDiscount
            pointStoreMenuAdapter.notifyDataSetChanged()
        }
        discountBottomSheet.show(parentFragmentManager, "DiscountBottomSheet")
    }

    private fun shownonDiscountBottomSheet(menu: PointDiscount) {
        val discountBottomSheet = DiscountBottomSheet(menu.pointDiscount) { selectedDiscount ->
            menu.pointDiscount = selectedDiscount
            pointStoreNonMenuAdapter.notifyDataSetChanged()
        }
        discountBottomSheet.show(parentFragmentManager, "DiscountBottomSheet")
    }


}
