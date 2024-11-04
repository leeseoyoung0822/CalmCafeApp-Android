package com.example.calmcafeapp.ui

import android.app.Dialog
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.calmcafeapp.R
import com.example.calmcafeapp.R.id.img_coupon
import com.example.calmcafeapp.base.BaseFragment
import com.example.calmcafeapp.data.CafeCouponData
import com.example.calmcafeapp.data.CafeMenuData
import com.example.calmcafeapp.data.MenuDetailResDto
import com.example.calmcafeapp.databinding.FragmentTaphomeBinding
import com.example.calmcafeapp.viewmodel.HomeViewModel
import com.example.calmcafeapp.viewmodel.RankViewModel

class TapHomeFragment : BaseFragment<FragmentTaphomeBinding>(R.layout.fragment_taphome) {
    private lateinit var menuCafeAdapter: MenuCafeAdapter
    private lateinit var couponCafeAdapter: CouponCafeAdapter
    private val rankViewModel: RankViewModel by activityViewModels()
    private val viewModel: HomeViewModel by activityViewModels()
    private var cafeImg : String? = null

    override fun initStartView() {
        super.initStartView()

        // 어댑터 초기화
        menuCafeAdapter = MenuCafeAdapter(ArrayList())
        couponCafeAdapter = CouponCafeAdapter(createDummyCouponData())

        // 리사이클러뷰 설정
        binding.menuList.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = menuCafeAdapter
        }
        binding.couponList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = couponCafeAdapter
        }

        // RecyclerView 스크롤 이벤트 처리
        binding.menuList.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0) {
                    // 스크롤할 때 BottomSheet가 확장되도록 처리 (옵션)
                }
            }
        })

        viewModel.cafeDetail.observe(viewLifecycleOwner) { cafeDetailResult ->
            if (cafeDetailResult != null) {
                // 카페 이름 설정

                cafeDetailResult.storeCongestionLevel?.let {
                    updateCircularProgress(binding.ownerCircularProgressViewBoss, it)
                }
                cafeDetailResult.userCongestionLevel?.let {
                    updateCircularProgress(binding.visitorCircularProgressViewVisitor, it)
                }
                cafeImg = cafeDetailResult.image

            }

        }

        viewModel.cafeMenuList.observe(viewLifecycleOwner) { menuList ->
            menuCafeAdapter.updateData(menuList.map { MenuDetailResDto(it.id, it.name, it.price, it.image) })
        }


        couponCafeAdapter.setMyItemClickListener(object : CouponCafeAdapter.MyItemClickListener {
            override fun onItemClick(menu: CafeCouponData) {
                showCouponPopup(menu)
            }
        })
    }

    override fun initDataBinding() {
        super.initDataBinding()

    }

    override fun initAfterBinding() {
        super.initAfterBinding()
    }

    // 쿠폰 더미 데이터 생성
    private fun createDummyCouponData(): ArrayList<CafeCouponData> {
        return arrayListOf(
            CafeCouponData(
                1,
                "Take-out",
                "10% 할인",
                "12:00 - 14:00"
            ),
            CafeCouponData(
                2,
                "매장 이용 시 아메리카노",
                "1+1",
                "15:30 - 16:30"
            ),
        )
    }


    private fun updateCircularProgress(view: CircularProgressView, crowdLevel: String) {
        val (percentage, text) = when (crowdLevel) {
            "CALM" -> 30f to "한산"
            "NORMAL" -> 60f to "보통"
            "BUSY" -> 100f to "혼잡"
            else -> 0f to "정보 없음"
        }
        view.setPercentage(percentage, text)
    }

    private fun showCouponPopup(coupon: CafeCouponData) {
        val dialog = Dialog(requireContext(), R.style.TransparentDialog)
        dialog.setContentView(R.layout.dialog_coupon)
        val imageView = dialog.findViewById<ImageView>(R.id.img_coupon)
        if (cafeImg != null) {
            // Glide 라이브러리를 사용하여 이미지 로드
            Glide.with(this)
                .load(cafeImg)
                .into(imageView)
        } else {
            // 이미지가 없을 경우 기본 이미지 설정
            imageView.setImageResource(R.drawable.cafe_img1)
        }
        dialog.findViewById<TextView>(R.id.couponTitle).text = "${coupon.coupon_type} ${coupon.sale} "
        dialog.findViewById<TextView>(R.id.couponExpiryDate).text = coupon.expiry_date
        dialog.show()
    }

}
