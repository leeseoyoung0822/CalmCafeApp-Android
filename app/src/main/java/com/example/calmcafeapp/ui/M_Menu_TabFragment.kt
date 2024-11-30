package com.example.calmcafeapp.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.view.menu.MenuAdapter
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.replace
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calmcafeapp.OwnerActivity
import com.example.calmcafeapp.R
import com.example.calmcafeapp.base.BaseFragment
import com.example.calmcafeapp.databinding.FragmentMMenuTabBinding
import com.example.calmcafeapp.databinding.FragmentMSettingBinding
import com.example.calmcafeapp.viewmodel.M_SettingViewModel

class M_Menu_TabFragment :  BaseFragment<FragmentMMenuTabBinding>(R.layout.fragment_m__menu__tab) {

    private val viewModel: M_SettingViewModel by activityViewModels()
    private lateinit var menuAdapter:M_MenuAdapter

    override fun initStartView() {
        super.initStartView()

        menuAdapter = M_MenuAdapter(
            emptyList(),
            onEditClick = { selectedMenu ->
                val editFragment = MenuEditFragment.newInstance(selectedMenu)
                requireActivity().supportFragmentManager.beginTransaction()
                    .replace(R.id.container_owner, editFragment)
                    .addToBackStack(null)
                    .commit()
            },
            onDeleteClick = { selectedMenu ->
                viewModel.deleteMenu(selectedMenu.id.toLong())
            }
        )

        binding.addMenuButton.setOnClickListener {
            requireActivity().supportFragmentManager.beginTransaction()
                .replace(R.id.container_owner, MenuAddFragment())
                .addToBackStack(null)
                .commit()
        }


        binding.menuRecyclerView.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = menuAdapter
        }
        Log.d("M_Menu_TabFragment", "Fetching menus from ViewModel")
        viewModel.fetchMenus()
    }

    override fun initDataBinding() {
        super.initDataBinding()

        // Observe menuListLiveData and update RecyclerView
        viewModel.menuListLiveData.observe(viewLifecycleOwner) { menuList ->
            menuAdapter.updateMenus(menuList)
        }

        // Observe error messages and display them
        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            // Show error message (e.g., Toast or Snackbar)
            // Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
        }

        viewModel.deleteMenuResult.observe(viewLifecycleOwner) { isSuccess ->
            if (isSuccess) {
                viewModel.fetchMenus() // 삭제 성공 시 메뉴 리스트 갱신
            }
        }



    }

}