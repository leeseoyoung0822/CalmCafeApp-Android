package com.example.calmcafeapp.ui

import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calmcafeapp.OwnerActivity
import com.example.calmcafeapp.R
import com.example.calmcafeapp.base.BaseFragment
import com.example.calmcafeapp.data.Promotion
import com.example.calmcafeapp.databinding.FragmentMPromotionTabBinding
import com.example.calmcafeapp.viewmodel.M_SettingViewModel

class M_Promotion_TabFragment : BaseFragment<FragmentMPromotionTabBinding>(R.layout.fragment_m__promotion__tab) {

    private val viewModel: M_SettingViewModel by activityViewModels()
    private lateinit var promotionAdapter: PromotionAdapter
    private var selectedPromotions: List<Promotion> = emptyList()

    override fun initStartView() {
        super.initStartView()

        promotionAdapter = PromotionAdapter(emptyList()) { selected ->
            selectedPromotions = selected
            binding.deleteButton.isEnabled = selectedPromotions.isNotEmpty()
        }
        binding.rvPromotion.layoutManager = LinearLayoutManager(requireContext())
        binding.rvPromotion.adapter = promotionAdapter

        // Fetch promotions
        viewModel.fetchPromotions()
    }

    override fun initDataBinding() {
        super.initDataBinding()

        // Observe promotions
        viewModel.promotionListLiveData.observe(viewLifecycleOwner) { promotions ->
            if (promotions != null && promotions.isNotEmpty()) {
                promotionAdapter.updateData(promotions)
                binding.tvPromotions.visibility = View.GONE
            } else {
                binding.tvPromotions.visibility = View.VISIBLE
            }
        }

        // Observe error messages
        viewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            if (errorMessage != null) {
                Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }
        }

        // 등록 버튼 클릭 시
        binding.addButton.setOnClickListener {
            val fragment = PromotionRegistrationFragment()
            (activity as OwnerActivity).addFragment(fragment)
        }

//        binding.deleteButton.setOnClickListener {
//            Log.d("selectedPromotions", "${selectedPromotions}")
//            if (selectedPromotions.isEmpty()) {
//                Toast.makeText(requireContext(), "삭제할 프로모션을 선택해주세요.", Toast.LENGTH_SHORT).show()
//            } else {
//                selectedPromotions.forEach { promotion ->
//                    viewModel.deletePromotion(promotion.id.toLong())
//                    selectedPromotions = emptyList()
//                }
//                Toast.makeText(requireContext(), "선택된 프로모션이 삭제되었습니다.", Toast.LENGTH_SHORT).show()
//
//                // 선택된 프로모션 초기화
//                selectedPromotions = emptyList()
//                binding.deleteButton.isEnabled = false
//
//                // 프로모션 리스트 갱신
//                viewModel.fetchPromotions()
//                promotionAdapter.updateData(emptyList())
//            }
//        }
        binding.deleteButton.setOnClickListener {
            if (selectedPromotions.isEmpty()) {
                Toast.makeText(requireContext(), "삭제할 프로모션을 선택해주세요.", Toast.LENGTH_SHORT).show()
            } else {
                selectedPromotions.forEach { promotion ->
                    viewModel.deletePromotion(promotion.id.toLong())
                }
                Toast.makeText(requireContext(), "선택된 프로모션이 삭제되었습니다.", Toast.LENGTH_SHORT).show()

                // 선택된 프로모션 초기화
                selectedPromotions = emptyList()
                binding.deleteButton.isEnabled = false

                // 프로모션 리스트 갱신
                viewModel.fetchPromotions()
            }
        }

    }

    override fun initAfterBinding() {
        super.initAfterBinding()
    }
}
