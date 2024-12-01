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
    private val viewModel: HomeViewModel by activityViewModels()
    private val rankViewModel: RankViewModel by activityViewModels()// ViewModel 가져오기
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var isFavorite = false
    private var listener: OnRouteStartListener? = null
    private var cafetitle : String = ""
    private var cafeadress : String = ""
    private var distance : Double? = 0.0

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

        // View 바인딩 초기화


        // Bundle에서 visibility 값을 가져옵니다.
        val visibility = arguments?.getString("visibility")

        // visibility 값에 따라 startBtn의 visibility를 설정합니다.
        if (visibility == "gone") {
            binding.startBtn.visibility = View.GONE
        } else {
            binding.startBtn.visibility = View.VISIBLE
        }


        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout
        val adapter = CafeDetailPagerAdapter(this)
        viewPager.adapter = adapter
        //listener?.onRouteStart()

        viewModel.distance.observe(viewLifecycleOwner) { distances ->
            binding.distance.text = "내 위치에서 ${distances}m"
            distance = distances

        }

        binding.startBtn.setOnClickListener {

            Log.d("distance111", "${distance}")

            if (distance!! <= 700.0) {
                // 700m 이하일 경우 도보 경로만 제공
                Toast.makeText(requireContext(), "700m 이하일 경우 도보 경로만 제공", Toast.LENGTH_SHORT).show()
                listener?.onRouteStart()
                dismiss()  // 현재 바텀시트

            } else {
                val navigatorFragment = NavigatorFragment().apply {
                    arguments = Bundle().apply {
                        putString("cafeTitle", cafetitle)
                    }
                }
                navigatorFragment.show(parentFragmentManager, "NavigatorFragment")
                listener?.onRouteStart()
                dismiss()  // 현재 바텀시트 닫기

            }

        }



        viewModel.cafeDetail.observe(viewLifecycleOwner) { cafeDetailResult ->
            if (cafeDetailResult != null) {
                // 카페 이름 설정
                binding.cafeName.text = cafeDetailResult.name
                Glide.with(this)
                    .load(cafeDetailResult.image) // 이미지 URL
                    .placeholder(R.drawable.img_loading) // 로딩 중 표시할 이미지
                    .error(R.drawable.img_error) // 로딩 실패 시 표시할 이미지
                    .into(binding.imageView5) // 이미지 뷰 대상
                cafetitle = cafeDetailResult.name
                cafeDetailResult.userCongestionLevel
                cafeDetailResult.storeCongestionLevel

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