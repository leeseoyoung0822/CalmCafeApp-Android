package com.example.calmcafeapp.ui

import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calmcafeapp.data.BottomSheetExpander
import com.example.calmcafeapp.databinding.FragmentNavigatorBinding
import com.example.calmcafeapp.viewmodel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class NavigatorFragment : BottomSheetDialogFragment(), BottomSheetExpander {

    private var _binding: FragmentNavigatorBinding? = null
    private val binding get() = _binding!!
    private val viewModel: HomeViewModel by activityViewModels()
    private lateinit var pathsAdapter: PathsAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentNavigatorBinding.inflate(inflater, container, false)
        return binding.root
    }
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        pathsAdapter = PathsAdapter(emptyList())
        binding.rvRouteInfo.layoutManager = LinearLayoutManager(context)
        binding.rvRouteInfo.adapter = pathsAdapter
        binding.rvRouteInfo.setHasFixedSize(true)

        viewModel.pubTransPaths.observe(viewLifecycleOwner) { paths ->
            Log.d("paths_navi1", "$paths")
            if (!paths.isNullOrEmpty()) {
                val subPaths = paths.first().subPath
                Log.d("paths_navi", "$subPaths")
                pathsAdapter.setSubPaths(subPaths)
                // totalTime 포맷팅하여 설정
                val totalTime = paths.first().info.totalTime
                formatTotalTime(totalTime)
                updateTimeViews(totalTime)

                val expectedArrivalTimeRange = calculateExpectedArrivalTimeRange(totalTime)
                binding.tvExDuration.text = expectedArrivalTimeRange
                // payment 설정
                val payment = paths.first().info.payment
                binding.tvCost.text = "|   금액 ${payment}원"
            } else {
                Handler().postDelayed({
                    if (viewModel.pubTransPaths.value.isNullOrEmpty()) {
                        // 여전히 null이라면 dismiss()
                        dismiss()
                        //Toast.makeText(requireContext(), "도보 경로 입니다.", Toast.LENGTH_SHORT).show()
                    }
                }, 1000) // 1초 대기
            }
        }

        viewModel.startAddress.observe(viewLifecycleOwner) { address ->
            binding.tvAddress.text = "내 위치"
//            binding.tvMylocation.text = "${address}..."


        }
        viewModel.destinationCafeName.observe(viewLifecycleOwner) { cafeName ->
            binding.tvAddress2.text = "도착\n${cafeName}"
//            binding.tvDestination.text = "$cafeName"
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

                // 레이아웃 높이를 wrap_content로 설정
                sheet.layoutParams.height = ViewGroup.LayoutParams.WRAP_CONTENT

                // 최대 높이 설정 (선택 사항)
                val maxHeight = (resources.displayMetrics.heightPixels * 0.9).toInt()  // 화면 높이의 90%
                if (sheet.measuredHeight > maxHeight) {
                    sheet.layoutParams.height = maxHeight
                }
                behavior.isHideable = false  // 바텀시트가 완전히 숨겨지지 않도록 설정
                behavior.isDraggable = true

                // 바텀시트 상태를 확장 상태로 설정
                behavior.state = BottomSheetBehavior.STATE_EXPANDED
            }
        }
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

    private fun formatTotalTime(totalTime: Int) {
        val hours = totalTime / 60
        val minutes = totalTime % 60

        // 시간/분 텍스트 뷰 가시성 및 데이터 설정
        if (hours > 0) {
            Log.d("hours", "${hours}")
            binding.tvDuration.text = "$hours"
            binding.tvDuration.visibility = View.VISIBLE
            binding.tvDurationText.visibility = View.VISIBLE
        } else {
            // 시간이 없으면 시간 관련 뷰를 숨김
            binding.tvDuration.visibility = View.GONE
            binding.tvDurationText.visibility = View.GONE
        }

        if (minutes > 0) {
            binding.tvMinutes.text = "$minutes" // 분 숫자만 표시
            binding.tvMinutesText.text = "분" // 분 텍스트 표시
            binding.tvMinutes.visibility = View.VISIBLE
            binding.tvMinutesText.visibility = View.VISIBLE
        } else {
            // 분이 없으면 분 관련 뷰를 숨김
            binding.tvMinutes.visibility = View.GONE
            binding.tvMinutesText.visibility = View.GONE
        }
    }


    private fun calculateExpectedArrivalTimeRange(totalTime: Int): String {
        val calendar = java.util.Calendar.getInstance()

        // 현재 시간
        val currentTimeMillis = System.currentTimeMillis()
        calendar.timeInMillis = currentTimeMillis

        val startTime = java.text.SimpleDateFormat("a h:mm", java.util.Locale.getDefault()).format(calendar.time)

        // 도착 시간 계산
        calendar.add(java.util.Calendar.MINUTE, totalTime)
        val endTime = java.text.SimpleDateFormat("a h:mm", java.util.Locale.getDefault()).format(calendar.time)

        return "$startTime - $endTime"
    }

    private fun updateTimeViews(totalTime: Int) {
        try {
            val currentTime = Calendar.getInstance()

            // 현재 시간 설정
            val currentFormatter = SimpleDateFormat("hh:mm", Locale.getDefault())
            val currentFormattedTime = currentFormatter.format(currentTime.time)
            Log.d("updateTimeViews", "$currentFormattedTime")
            binding.currentTime.text = currentFormattedTime // 현재 시간 TextView에 설정

            currentTime.add(Calendar.MINUTE, totalTime)
            val expectedFormatter = SimpleDateFormat("hh:mm", Locale.getDefault())
            val expectedFormattedTime = expectedFormatter.format(currentTime.time)
            binding.expectedTime.text = expectedFormattedTime // 예상 시간 TextView에 설정
            Log.d("updateTimeViews", "${expectedFormattedTime}")

            binding.tvExDuration.text = "도착 예상 시각: $expectedFormattedTime"
        } catch (e: Exception) {
            Log.e("NavigatorFragment", "Error in updateTimeViews: ${e.message}")
        }
    }


    private fun dismissBottomSheet() {
        dialog?.let { dialog ->
            val bottomSheet = dialog.findViewById<View>(com.google.android.material.R.id.design_bottom_sheet)
            bottomSheet?.let { sheet ->
                val behavior = BottomSheetBehavior.from(sheet)
                behavior.state = BottomSheetBehavior.STATE_HIDDEN  // 바텀 시트를 숨김 상태로 변경
            }
        }
    }
}