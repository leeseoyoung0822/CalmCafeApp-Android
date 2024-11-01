package com.example.calmcafeapp.ui

import android.view.LayoutInflater
import android.view.View
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calmcafeapp.R
import com.example.calmcafeapp.base.BaseFragment
import com.example.calmcafeapp.databinding.FragmentPointMenuBinding
import com.example.calmcafeapp.databinding.ModalCouponAddedBinding
import com.example.calmcafeapp.databinding.ModalCouponPurchaseBinding
import com.example.calmcafeapp.viewmodel.RankViewModel
import com.google.android.material.dialog.MaterialAlertDialogBuilder

class PointMenuFragment : BaseFragment<FragmentPointMenuBinding>(R.layout.fragment_point_menu) {

    private lateinit var pointMenuAdapter: PointMenuAdapter
    private val rankViewModel: RankViewModel by activityViewModels()

    override fun initStartView() {
        super.initStartView()

        // 어댑터 초기화 (초기 빈 리스트로 설정)
        pointMenuAdapter = PointMenuAdapter(arrayListOf()) { couponName ->
            showFirstDialog(couponName) // "사용" 버튼 클릭 시 첫 번째 다이얼로그 표시
        }

        // 리사이클러뷰 설정
        binding.storeRecyclerview.apply {
            layoutManager = LinearLayoutManager(context)
            layoutManager = GridLayoutManager(context, 2) // 2열 그리드 레이아웃 설정
            adapter = pointMenuAdapter
        }
    }

    override fun initDataBinding() {
        super.initDataBinding()

        // ViewModel의 pointMenuDetailList 관찰하여 RecyclerView에 데이터 바인딩
        rankViewModel.pointMenuDetailList.observe(viewLifecycleOwner) { pointMenuList ->
            pointMenuAdapter.updatePointMenuList(pointMenuList)
        }
    }

    override fun initAfterBinding() {
        super.initAfterBinding()
        // 추가 작업이 필요하면 여기에 설정
    }

    private fun showFirstDialog(couponName: String) {
        val dialogBinding = ModalCouponPurchaseBinding.inflate(LayoutInflater.from(context))
        val alertDialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogBinding.root)
            .create()

        // X 버튼 클릭 이벤트
        dialogBinding.closeButton.setOnClickListener {
            alertDialog.dismiss()
        }

        // "네" 버튼 클릭 시 두 번째 다이얼로그 표시
        dialogBinding.btnYes.setOnClickListener {
            alertDialog.dismiss()
            showSecondDialog(couponName)
        }

        // "다음에" 버튼 클릭 시 다이얼로그 닫기
        dialogBinding.btnLater.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }

    private fun showSecondDialog(couponName: String) {
        val dialogBinding = ModalCouponAddedBinding.inflate(LayoutInflater.from(context))
        val alertDialog = MaterialAlertDialogBuilder(requireContext())
            .setView(dialogBinding.root)
            .create()

        dialogBinding.closeButton.setOnClickListener {
            alertDialog.dismiss()
        }

        dialogBinding.btnGoToCoupons.setOnClickListener {
            alertDialog.dismiss()

        }

        dialogBinding.btnNoThanks.setOnClickListener {
            alertDialog.dismiss()
        }

        alertDialog.show()
    }
}