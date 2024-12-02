package com.example.calmcafeapp.ui

import android.annotation.SuppressLint
import android.app.Dialog
import android.view.LayoutInflater
import android.widget.PopupWindow
import android.view.Gravity
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.calmcafeapp.R
import com.example.calmcafeapp.base.BaseFragment
import com.example.calmcafeapp.data.CafeCouponData
import com.example.calmcafeapp.data.MenuDetailResDto
import com.example.calmcafeapp.data.PromotionDetailResDto
import com.example.calmcafeapp.data.TimeDetail
import com.example.calmcafeapp.databinding.FragmentTaphomeBinding
import com.example.calmcafeapp.viewmodel.HomeViewModel

class TapHomeFragment : BaseFragment<FragmentTaphomeBinding>(R.layout.fragment_taphome) {
    private lateinit var menuCafeAdapter: MenuCafeAdapter
    private lateinit var couponCafeAdapter: CouponCafeAdapter
    private val viewModel: HomeViewModel by activityViewModels()
    private var cafeImg : String? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun initStartView() {
        super.initStartView()

        // 어댑터 초기화
        menuCafeAdapter = MenuCafeAdapter(ArrayList())
        couponCafeAdapter = CouponCafeAdapter(createDummyPromotionData())

        // 리사이클러뷰 설정
        binding.menuList.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = menuCafeAdapter
        }
        binding.couponList.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = couponCafeAdapter
        }

        binding.infoOwnerCongestion.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                showTooltip(v, "실시간 매장 상황을 AI로 분석한 실시간 좌석 이용률입니다.")
            }
            true
        }

        binding.infoVisitorCongestion.setOnTouchListener { v, event ->
            if (event.action == MotionEvent.ACTION_DOWN) {
                showTooltip(v, "실제 방문한 손님들이 직접 입력한 매장 혼잡도입니다.")
            }
            true
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
//                cafeDetailResult.promotionDetailResDtoList?.let{
//                    upda
//                }
                cafeImg = cafeDetailResult.image
            }
        }

        viewModel.cafeMenuList.observe(viewLifecycleOwner) { menuList ->
            menuCafeAdapter.updateData(menuList.map { MenuDetailResDto(it.id, it.name, it.price, it.image) })
        }


        couponCafeAdapter.setMyItemClickListener(object : CouponCafeAdapter.MyItemClickListener {
            override fun onItemClick(menu: PromotionDetailResDto) {
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

    private fun showTooltip(view: View, message: String) {
        // 툴팁 레이아웃 설정
        val popupView = LayoutInflater.from(requireContext()).inflate(R.layout.tooltip_layout, null)
        val popupWindow = PopupWindow(popupView, ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT)
        popupView.findViewById<TextView>(R.id.tooltip_text).text = message

        // 팝업 외부 터치 시 닫히도록 설정
        popupWindow.isOutsideTouchable = true

        // 툴팁 위치를 계산
        val location = IntArray(2)
        view.getLocationOnScreen(location)
        val x = location[0] + view.width / 2 - popupView.measuredWidth / 2
        val y = location[1] - view.height // Y 좌표 (뷰의 위쪽에 위치하도록)

        popupWindow.showAtLocation(view, Gravity.NO_GRAVITY, x, y - 35)
    }


    private fun createDummyPromotionData(): ArrayList<PromotionDetailResDto> {
        return arrayListOf(
            PromotionDetailResDto(
                id = 1,
                startTime = "07:00",
                endTime = "10:00",
                discount = 10,
                promotionType = "모닝 커피 할인",
                promotionUsedState = "사용 가능"
            ),
            PromotionDetailResDto(
                id = 3,
                startTime = "11:30",
                endTime = "13:10",
                discount = 15,
                promotionType = "점심 시간 할인",
                promotionUsedState = "사용 가능"
            )
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

    private fun showCouponPopup(promotion: PromotionDetailResDto) {
        val dialog = Dialog(requireContext(), R.style.TransparentDialog)
        dialog.setContentView(R.layout.dialog_coupon) // 사용하신 XML 파일

        // 다이얼로그 UI 요소 참조
        val imageView = dialog.findViewById<ImageView>(R.id.img_coupon)
        val titleTextView = dialog.findViewById<TextView>(R.id.couponTitle)
        val expiryTextView = dialog.findViewById<TextView>(R.id.couponExpiry)
        val expiryDateTextView = dialog.findViewById<TextView>(R.id.couponExpiryDate)
        val useCouponButton = dialog.findViewById<View>(R.id.btnUseCoupon)

        // 이미지 로드 (기본 이미지 또는 특정 리소스 사용)
        if (cafeImg != null) {
            Glide.with(this)
                .load(cafeImg)
                .into(imageView)
        } else {
            imageView.setImageResource(R.drawable.coupon_img) // 기본 이미지
        }

        // 시간을 두 자리 형식으로 변환
        val startTime = promotion.startTime

        val endTime = promotion.endTime

        // 프로모션 데이터를 다이얼로그에 설정
        titleTextView.text = promotion.promotionType
        expiryDateTextView.text = "유효기간: $startTime ~ $endTime"

        // 다이얼로그 표시
        dialog.show()
    }


}
