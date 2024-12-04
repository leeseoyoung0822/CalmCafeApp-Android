package com.example.calmcafeapp.ui

import GridSpacingWithDividerDecoration
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.calmcafeapp.R
import com.example.calmcafeapp.base.BaseFragment
import com.example.calmcafeapp.data.CafeMenuData
import com.example.calmcafeapp.data.MenuDetailResDto
import com.example.calmcafeapp.data.PointMenuDetailResDto
import com.example.calmcafeapp.databinding.FragmentStoreBinding
import com.example.calmcafeapp.databinding.FragmentTaphomeBinding
import com.example.calmcafeapp.viewmodel.HomeViewModel

class StoreFragment : BaseFragment<FragmentStoreBinding>(R.layout.fragment_store) {
    private lateinit var menuPointStoreAdapter: MenuPointStoreAdapter
    private val viewModel: HomeViewModel by activityViewModels()
    private var userPoints: Int = 0

    override fun initStartView() {
        super.initStartView()
        menuPointStoreAdapter = MenuPointStoreAdapter(createDummyData())
        binding.rvPointmenu.apply {
            layoutManager = GridLayoutManager(requireContext(), 2) // 2열 그리드 레이아웃
            adapter = menuPointStoreAdapter

            // ItemDecoration 추가
            addItemDecoration(
                GridSpacingWithDividerDecoration(
                    spanCount = 2,
                    spacing = 20, // 각 아이템 간의 간격
                    dividerHeight = 0, // 구분선 높이
                    dividerColor = requireContext().getColor(R.color.dividerColor) // 구분선 색상
                )
            )
        }

        // 클릭 리스너 설정
        menuPointStoreAdapter.setMyItemClickListener(object : MenuPointStoreAdapter.MyItemClickListener {
            override fun onItemClick(menu: PointMenuDetailResDto) {
                showPurchaseDialog(menu)
            }
        })

        viewModel.pointMenuList.observe(viewLifecycleOwner) { pointMenuList ->
            menuPointStoreAdapter.updateData(pointMenuList.map { PointMenuDetailResDto(it.id, it.name, it.pointPrice, it.pointDiscount,it.image) })
        }
        viewModel.cafeDetail.observe(viewLifecycleOwner) { cafeDetailResult ->
            userPoints = cafeDetailResult?.point ?: 0
            binding.point.text = "보유 포인트: ${cafeDetailResult?.point.toString()}P"
            }
        viewModel.userPointsLiveData.observe(viewLifecycleOwner) { newPoints ->
            userPoints = newPoints
            binding.point.text = "보유 포인트: ${userPoints}P"
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

    @SuppressLint("MissingInflatedId")
    private fun showPurchaseDialog(menu: PointMenuDetailResDto) {

        val dialogView = LayoutInflater.from(requireContext()).inflate(R.layout.dialog_purchase, null)
        val builder = AlertDialog.Builder(requireContext(), R.style.CustomDialogStyle)
            .setView(dialogView)


        val dialog = builder.create()

        // 다이얼로그의 뷰 요소 참조
        val imageView = dialogView.findViewById<ImageView>(R.id.menu_image)
        val nameTextView = dialogView.findViewById<TextView>(R.id.menu_name)
        val priceTextView = dialogView.findViewById<TextView>(R.id.menu_price)
        val purchaseButton = dialogView.findViewById<Button>(R.id.btn_purchase)
        val cancelButton = dialogView.findViewById<Button>(R.id.btn_cancel)

        // 데이터 설정
        Glide.with(this)
            .load(menu.image)
            .into(imageView)
        nameTextView.text = menu.name
        priceTextView.text = "${menu.pointPrice}P"

        // 버튼 클릭 이벤트 처리
        purchaseButton.setOnClickListener {
            if (userPoints >= menu.pointPrice) {
//                // 포인트가 충분한 경우
                viewModel.purchaseItem(menu.id)
                Toast.makeText(requireContext(), "${menu.name}을 구매하였습니다.", Toast.LENGTH_SHORT).show()
                dialog.dismiss()
            } else {
                // 포인트가 부족한 경우
                Toast.makeText(requireContext(), "포인트가 부족합니다.", Toast.LENGTH_SHORT).show()
            }
        }

        cancelButton.setOnClickListener {
            dialog.dismiss()
        }

        dialog.show()
    }

}