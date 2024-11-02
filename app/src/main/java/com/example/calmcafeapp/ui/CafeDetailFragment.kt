package com.example.calmcafeapp.ui

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.viewpager2.widget.ViewPager2
import com.bumptech.glide.Glide
import com.example.calmcafeapp.R
import com.example.calmcafeapp.UserActivity
import com.example.calmcafeapp.data.BottomSheetExpander
import com.example.calmcafeapp.data.OnRouteStartListener
import com.example.calmcafeapp.databinding.FragmentCafeDetailBinding
import com.example.calmcafeapp.viewmodel.HomeViewModel
import com.example.calmcafeapp.viewmodel.RankViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayoutMediator
import java.text.SimpleDateFormat
import java.util.Locale

class CafeDetailFragment : BottomSheetDialogFragment(), BottomSheetExpander {

    private var _binding: FragmentCafeDetailBinding? = null
    private val binding get() = _binding!!
    private val homeViewModel: HomeViewModel by activityViewModels()// ViewModel 가져오기
    private val rankViewModel: RankViewModel by activityViewModels()// ViewModel 가져오기
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var isFavorite = false
    private var listener: OnRouteStartListener? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // NavigatorFragment로부터 결과를 받기 위한 리스너 설정
        parentFragmentManager.setFragmentResultListener("navigatorResultKey", this) { key, bundle ->
            val resultData = bundle.getString("resultData")
            // 필요한 작업 수행
        }
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCafeDetailBinding.inflate(inflater, container, false)
        return binding.root

    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val storeId = arguments?.getInt("storeId") ?: return
        val latitude = arguments?.getDouble("latitude") ?: return
        val longitude = arguments?.getDouble("longitude") ?: return

        // ViewModel에서 카페 정보를 가져옴
                homeViewModel.fetchCafeDetailInfo(storeId, latitude, longitude)

        // LiveData 관찰하여 UI 업데이트
        homeViewModel.cafeDetail.observe(viewLifecycleOwner) { cafeDetail ->
            cafeDetail?.let {
                binding.cafeName.text = it.name
                // 기타 UI 업데이트
            }
        }

        homeViewModel.errorMessage.observe(viewLifecycleOwner) { errorMessage ->
            errorMessage?.let {
                Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            }
        }

        // 좋아요 버튼 상태 업데이트 (favoriteStoreId 관찰)
        rankViewModel.favoriteStoreId.observe(viewLifecycleOwner) { updatedStoreId ->
            if (updatedStoreId == storeId) {
                isFavorite = !isFavorite // 현재 상태 반전
                updateLikeButton(isFavorite)
            }
        }

        // 출발하기 버튼 클릭 리스너 설정
        binding.startBtn.setOnClickListener {
            // 버튼이 눌리면 인터페이스 메서드 호출
            // 네비게이션
            (activity as?UserActivity)?.addFragment(NavigatorFragment())
            listener?.onRouteStart()
            dismiss() // 바텀 시트 닫기
        }

        // 좋아요 버튼 클릭 리스너 설정 (옵션)
        binding.likesBtn.setOnClickListener {
            if (isFavorite) {
                rankViewModel.removeFavorite(storeId)
            } else {
                rankViewModel.addFavorite(storeId)
            }
            isFavorite = !isFavorite // 로컬 상태 업데이트
            updateLikeButton(isFavorite)
        }


        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout
        val adapter = CafeDetailPagerAdapter(this)
        viewPager.adapter = adapter


        expandBottomSheet()
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "홈"
                1 -> "추천카페"
                2 -> "스토어"
                else -> null
            }
        }.attach()

        binding.startBtn.setOnClickListener {
            val distance = binding.startBtn.tag as? Int  // 거리 정보를 가져옴

                if (distance != null && distance <= 700) {
                    // 700m 이하일 경우 도보 경로만 제공
                    Toast.makeText(requireContext(), "700m 이하일 경우 도보 경로만 제공합니다.", Toast.LENGTH_SHORT).show()
                    //listener?.onRouteStart()  // 도보 경로를 시작하도록 알림
                    dismiss()  // 현재 바텀시트 닫기
                } else {
                    // NavigatorFragment를 표시
                    val navigatorFragment = NavigatorFragment().apply {
                        arguments = Bundle().apply {
                            //putString("cafeTitle", cafeTitle)
                            //putString("cafeAddress", cafeAddress)
                        }
                    }
                    listener?.onRouteStart()
                    dismiss()  // 현재 바텀시트 닫기
                    navigatorFragment.show(parentFragmentManager, "NavigatorFragment")
                }
        }
    }

    override fun expandBottomSheet() {
        dialog?.let { dialog ->
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let { sheet ->
                val behavior = BottomSheetBehavior.from(sheet)
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED  // COLLAPSED로 설정
            }
        }
    }

    private fun updateLikeButton(isLiked: Boolean) {
        binding.likesBtn.setImageResource(if (isLiked) R.drawable.heart_filled else R.drawable.heart_empty)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onStart() {
        super.onStart()
        dialog?.let { dialog ->
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let { sheet ->
                val behavior = BottomSheetBehavior.from(sheet)

                // 화면 높이 계산
                val displayMetrics = resources.displayMetrics
                val screenHeight = displayMetrics.heightPixels

                // peekHeight를 화면의 절반 높이로 설정
                val halfHeight = screenHeight / 2
                behavior.peekHeight = halfHeight

                // 초기 상태를 COLLAPSED로 설정
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED

                behavior.isFitToContents = false
                behavior.maxHeight = screenHeight
                behavior.skipCollapsed = false
                behavior.isDraggable = true


            }
        }
    }

}