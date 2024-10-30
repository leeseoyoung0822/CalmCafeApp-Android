package com.example.calmcafeapp.ui

import android.content.Context
import android.content.DialogInterface
import android.os.Bundle
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.viewpager2.widget.ViewPager2
import com.example.calmcafeapp.R
import com.example.calmcafeapp.UserActivity
import com.example.calmcafeapp.data.BottomSheetExpander
import com.example.calmcafeapp.data.OnRouteStartListener
import com.example.calmcafeapp.databinding.FragmentCafeDetailBinding
import com.example.calmcafeapp.viewmodel.RankViewModel
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.google.android.material.tabs.TabLayoutMediator
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class CafeDetailFragment : BottomSheetDialogFragment(), BottomSheetExpander {

    private var _binding: FragmentCafeDetailBinding? = null
    private val binding get() = _binding!!
    private val rankViewModel: RankViewModel by activityViewModels() // ViewModel 가져오기
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var isFavorite = false
    // 라우트 시작을 위한 리스너
    private var listener: OnRouteStartListener? = null

    /*override fun onAttach(context: Context) {
        super.onAttach(context)
        // targetFragment가 OnRouteStartListener를 구현하고 있는지 확인
        listener = parentFragment as? OnRouteStartListener ?: context as? OnRouteStartListener
        if (listener == null) {
            Log.e("CafeDetailFragment", "OnRouteStartListener가 구현되지 않았습니다. 일부 기능이 제한될 수 있습니다.")
        }
    }*/

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // View Binding을 사용하여 레이아웃 인플레이트
        _binding = FragmentCafeDetailBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // 전달된 데이터 받기
        /*val cafeTitle = arguments?.getString("cafeTitle") ?: "카페 이름 없음"
        binding.cafeName.text = cafeTitle*/

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(requireActivity())
        // 전달받은 storeId 가져오기
        val storeId = arguments?.getInt("storeId") ?: return
        Log.d("CafeDetailFragment", "Store ID: $storeId")

        // 사용자 위치 가져오기
        fusedLocationClient.lastLocation.addOnSuccessListener { location ->
            if (location != null) {
                // ViewModel을 통해 API 호출 (위도와 경도를 함께 전달)
                rankViewModel.fetchCafeDetail(storeId, location.latitude, location.longitude)
            } else {
                // 위치를 가져올 수 없는 경우 예외 처리
                Log.e("CafeDetailFragment", "사용자 위치 정보를 가져올 수 없습니다.")
            }
        }
        // API 응답 관찰 및 UI 업데이트
        rankViewModel.cafeDetail.observe(viewLifecycleOwner) { cafeDetail ->
            cafeDetail?.let {
                binding.cafeName.text = cafeDetail.name
                binding.likesNum.text = "${cafeDetail.favoriteCount}개"
                binding.openTime.text = formatTime(cafeDetail.openingTime)
                // 거리 변환 후 TextView에 설정
                binding.distance.text = formatDistance(cafeDetail.distance)
                isFavorite = cafeDetail.isFavorite
                updateLikeButton(isFavorite)
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

        // ViewPager2와 TabLayout 설정
        val viewPager = binding.viewPager
        val tabLayout = binding.tabLayout
        val adapter = CafeDetailPagerAdapter(this)
        viewPager.adapter = adapter

        // TabLayout과 ViewPager2 연결
        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "홈"
                1 -> "추천카페"
                2 -> "스토어"
                else -> null
            }
        }.attach()

        // ViewPager의 페이지가 변경될 때 BottomSheet를 전체 화면으로 확장
        viewPager.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                expandBottomSheet()  // 페이지가 바뀔 때 바텀 시트를 확장
            }
        })
    }

    override fun expandBottomSheet() {
        dialog?.let { dialog ->
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let { sheet ->
                val behavior = BottomSheetBehavior.from(sheet)
                behavior.state = BottomSheetBehavior.STATE_EXPANDED  // 바텀 시트를 전체 화면으로 확장
            }
        }
    }
    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        rankViewModel.resetFavoriteStoreId()
    }
    private fun updateLikeButton(isLiked: Boolean) {
        binding.likesBtn.setImageResource(if (isLiked) R.drawable.heart_filled else R.drawable.heart_empty)
    }

    private fun formatTime(timeString: String): String {
        return try {
            val dateFormat = SimpleDateFormat("HH:mm:ss", Locale.getDefault())
            val date = dateFormat.parse(timeString)
            val calendar = Calendar.getInstance().apply { time = date }
            "${calendar.get(Calendar.HOUR_OF_DAY)}:${calendar.get(Calendar.MINUTE)} 오픈"
        } catch (e: Exception) {
            Log.e("CafeDetailFragment", "시간 형식 파싱 오류: ${e.message}")
            ""
        }
    }

    private fun formatDistance(distanceInMeters: Double): String {
        return if (distanceInMeters >= 1000) {
            "내 위치에서  %.1fkm".format(distanceInMeters/1000.0)
        } else {
            "내 위치에서 ${distanceInMeters}m"
        }
    }



    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null // 메모리 누수 방지를 위해 바인딩 해제
    }

    override fun onStart() {
        super.onStart()
        dialog?.let { dialog ->
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let { sheet ->
                val behavior = BottomSheetBehavior.from(sheet)

                // 화면의 절반 높이를 계산
                val displayMetrics = DisplayMetrics()
                requireActivity().windowManager.defaultDisplay.getMetrics(displayMetrics)
                val screenHeight = displayMetrics.heightPixels
                val halfHeight = screenHeight / 3

                // 피크 높이를 절반으로 설정
                behavior.peekHeight = halfHeight

                // 바텀 시트의 상태를 콜랩스 상태로 설정
                behavior.state = BottomSheetBehavior.STATE_COLLAPSED

                // 바텀 시트가 전체 화면을 채우도록 설정
                behavior.isFitToContents = true // 콘텐츠에 맞추지 않도록 설정
                behavior.maxHeight = screenHeight // 최대 높이를 화면 높이로 설정
                behavior.skipCollapsed = false // 콜랩스 상태를 건너뛰지 않음
                behavior.isDraggable = true // 드래그 가능하도록 설정
            }
        }
    }



//    private fun showrouteInfo(route: LocalItem) {
//        selectedroute = route  // 선택된 카페 정보 저장
//        val navigatorFragment = NavigatorFragment()
//        navigatorFragment.setTargetFragment(this, 0)
//        // 카페 정보를 프래그먼트로 전달
//        val bundle = Bundle().apply {
//            putString("cafeTitle", cafe.title)
//            putString("cafeAddress", cafe.address)
//            // 필요한 다른 정보도 여기에 추가
//        }
//        cafeDetailFragment.arguments = bundle
//        cafeDetailFragment.show(parentFragmentManager, "CafeDetailFragment")
//    }
}
