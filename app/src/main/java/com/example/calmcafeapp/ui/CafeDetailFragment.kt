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
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayoutMediator
import java.text.SimpleDateFormat
import java.util.Locale

class CafeDetailFragment : BottomSheetDialogFragment(), BottomSheetExpander {

    private var _binding: FragmentCafeDetailBinding? = null
    private val binding get() = _binding!!

    private var listener: OnRouteStartListener? = null
    private val viewModel: HomeViewModel by activityViewModels()

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = targetFragment as? OnRouteStartListener
        if (listener == null) {
            throw ClassCastException("$targetFragment must implement OnRouteStartListener")
        }
    }

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

    @SuppressLint("SetTextI18n")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val cafeTitle = arguments?.getString("cafeTitle") ?: "카페 이름 없음"
        val cafeAddress = arguments?.getString("cafeAddress") ?: "카페 주소 없음"
        binding.cafeName.text = cafeTitle


        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout
        val adapter = CafeDetailPagerAdapter(this)
        viewPager.adapter = adapter

        // 내 위치 계사
        viewModel.pubTransPaths.observe(viewLifecycleOwner) { paths ->
            Log.d("paths_navi", "$paths")
            if (paths != null && paths.isNotEmpty()) {
                val distance = paths.first().info.trafficDistance
                binding.distance.text = "내 위치에서 ${distance}m"
                binding.startBtn.tag = distance
            } else {
                Toast.makeText(requireContext(), "로딩 중..", Toast.LENGTH_SHORT).show()
            }
        }

        viewModel.cafeDetail.observe(viewLifecycleOwner) { cafeDetailResult ->
            if (cafeDetailResult != null) {
                // 카페 이름 설정
                binding.cafeName.text = cafeDetailResult.name
                binding.likesNum.text = cafeDetailResult.favoriteCount.toString()

//
//                Glide.with(this)
//                    .load(cafeDetailResult.image)
//                    .placeholder(R.drawable.placeholder_image) // 기본 이미지
//                    .error(R.drawable.error_image) // 오류 시 표시할 이미지
//                    .into(binding.ivCafeImage)
            }
        }

        // 거리 관찰
        viewModel.formattedDistance.observe(viewLifecycleOwner) { formattedDistance ->
            binding.distance.text = "내 위치에서 $formattedDistance"
        }

        // 영업 시간 관찰
        viewModel.formattedOpeningTime.observe(viewLifecycleOwner) { openingTime ->
            val closingTime = viewModel.formattedClosingTime.value ?: "정보 없음"
            binding.openState.text = "영업 시간: $openingTime - $closingTime"
        }

        viewModel.formattedClosingTime.observe(viewLifecycleOwner) { closingTime ->
            val openingTime = viewModel.formattedOpeningTime.value ?: "정보 없음"
            binding.openState.text = "영업 시간: $openingTime - $closingTime"
        }


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
                            putString("cafeTitle", cafeTitle)
                            putString("cafeAddress", cafeAddress)
                        }
                    }
                    listener?.onRouteStart()
                    dismiss()  // 현재 바텀시트 닫기
                    navigatorFragment.show(parentFragmentManager, "NavigatorFragment")
                }
        }

        // 좋아요 버튼 클릭 리스너
        binding.likesBtn.setOnClickListener {
            toggleLike()
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

    private fun toggleLike() {
        val isLiked = binding.likesBtn.tag as? Boolean ?: false
        if (isLiked) {
            binding.likesBtn.setImageResource(R.drawable.heart_empty)
            val currentLikes = binding.likesNum.text.toString().replace("개", "").toIntOrNull() ?: 0
            binding.likesNum.text = "${currentLikes - 1}개"
        } else {
            binding.likesBtn.setImageResource(R.drawable.heart_filled)
            val currentLikes = binding.likesNum.text.toString().replace("개", "").toIntOrNull() ?: 0
            binding.likesNum.text = "${currentLikes + 1}개"
        }
        binding.likesBtn.tag = !isLiked
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