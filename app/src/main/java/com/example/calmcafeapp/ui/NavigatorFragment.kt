package com.example.calmcafeapp.ui

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.calmcafeapp.data.BottomSheetExpander
import com.example.calmcafeapp.databinding.FragmentNavigatorBinding
import com.example.calmcafeapp.viewmodel.HomeViewModel
import com.google.android.material.bottomsheet.BottomSheetBehavior
import com.google.android.material.bottomsheet.BottomSheetDialogFragment

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

        pathsAdapter = PathsAdapter(emptyList())  // 초기에는 빈 리스트로 설정
        binding.rvRouteInfo.layoutManager = LinearLayoutManager(context)
        binding.rvRouteInfo.adapter = pathsAdapter
        binding.rvRouteInfo.setHasFixedSize(true)

        viewModel.pubTransPaths.observe(viewLifecycleOwner) { paths ->
            Log.d("paths_navi", "$paths")
            if (paths != null && paths.isNotEmpty()) {
                val subPaths = paths.first().subPath
                Log.d("paths_navi", "$subPaths")
                pathsAdapter.setSubPaths(subPaths)
                // totalTime 포맷팅하여 설정
                val totalTime = paths.first().info.totalTime
                binding.tvDuration.text = formatTotalTime(totalTime)

                // payment 설정
                val payment = paths.first().info.payment
                binding.tvCost.text = "${payment}원"
            } else {
                // 경로 데이터가 없는 경우 처리
                //Toast.makeText(requireContext(), "로딩 중..", Toast.LENGTH_SHORT).show()
            }
        }
        viewModel.startAddress.observe(viewLifecycleOwner) { address ->
            binding.tvAddress.text = "$address"
        }
        viewModel.destinationCafeName.observe(viewLifecycleOwner) { cafeName ->
            binding.tvAddress2.text = "$cafeName"
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

    private fun formatTotalTime(totalTime: Int): String {
        return if (totalTime >= 60) {
            val hours = totalTime / 60
            val minutes = totalTime % 60
            if (minutes > 0) {
                "${hours}시간 ${minutes}분"
            } else {
                "${hours}시간"
            }
        } else {
            "${totalTime}분"
        }
    }
}