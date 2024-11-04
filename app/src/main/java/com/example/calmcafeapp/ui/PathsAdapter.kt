package com.example.calmcafeapp.ui

import android.graphics.Color
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calmcafeapp.R
import com.example.calmcafeapp.data.Path
import com.example.calmcafeapp.data.SubPath
import com.example.calmcafeapp.databinding.ItemTransitBinding
import com.example.calmcafeapp.databinding.ItemWalkBinding

class PathsAdapter(private var subPaths: List<SubPath>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        private const val VIEW_TYPE_WALKING = 0
        private const val VIEW_TYPE_TRANSIT = 1
    }

    override fun getItemViewType(position: Int): Int {
        return when (subPaths[position].trafficType) {
            3 -> VIEW_TYPE_WALKING
            1, 2 -> VIEW_TYPE_TRANSIT
            else -> throw IllegalArgumentException("Invalid traffic type: ${subPaths[position].trafficType}")
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_WALKING) {
            val binding = ItemWalkBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            WalkingViewHolder(binding)
        } else {
            val binding = ItemTransitBinding.inflate(LayoutInflater.from(parent.context), parent, false)
            TransitViewHolder(binding)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val subPath = subPaths[position]
        if (holder is WalkingViewHolder) {
            holder.bind(subPath)
        } else if (holder is TransitViewHolder) {
            holder.bind(subPath)
        }
    }

    override fun getItemCount(): Int = subPaths.size

    // ViewHolder 클래스들
    inner class WalkingViewHolder(private val binding: ItemWalkBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(subPath: SubPath) {
            val distance = subPath.distance?.toInt() ?: 0
            val time = subPath.sectionTime ?: 0
            binding.tvWalkInfo.text = "${distance}m, ${time}분"
            binding.ivTransportIcon.setImageResource(R.drawable.walking_72672)
            // 추가적인 설정이 필요하면 여기서 처리
        }
    }

    inner class TransitViewHolder(private val binding: ItemTransitBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(subPath: SubPath) {
            val transportType = when (subPath.trafficType) {
                1 -> "지하철"
                2 -> "버스"
                else -> "알 수 없음"
            }
            val lane = subPath.lane?.firstOrNull()
            val transportNumber = lane?.busNo ?: lane?.name ?: ""
            val sectionTime = subPath.sectionTime ?: 0
            val stationCount = subPath.stationCount ?: 0
            val transportType1 = when (subPath.trafficType) {
                1 -> {  binding.tvTransitType.text = "$transportType\n${transportNumber}"
                        binding.tvTransitDetails.text = "${sectionTime}분, ${stationCount}정류장"}
                2 -> {  binding.tvTransitType.text = "$transportType\n${transportNumber}번"
                        binding.tvTransitDetails.text = "${sectionTime}분, ${stationCount}정거장" }
                else -> {}
            }



            // 아이콘 설정
            val iconRes = when (subPath.trafficType) {
                1 -> R.drawable.ic_tran
                2 -> R.drawable.ic_tran
                else -> R.drawable.walking_72672
            }
            binding.ivTransportIcon.setImageResource(iconRes)

            // 선 색상 설정
            val lineColor = when (subPath.trafficType) {
                1 -> Color.GREEN  // 지하철
                2 -> Color.BLUE   // 버스
                else -> Color.GRAY
            }
            binding.viewLine.setBackgroundColor(lineColor)
        }
    }

    // 업데이트를 위한 메서드 추가
    fun setSubPaths(newSubPaths: List<SubPath>) {
        // 도보 항목 중 거리 0m인 항목 제외
        this.subPaths = newSubPaths.filter { subPath ->
            if (subPath.trafficType == 3) { // 도보 항목
                val distance = subPath.distance?.toInt() ?: 0
                distance > 0 // 거리 > 0m인 경우만 포함
            } else {
                true // 도보 외의 항목은 모두 포함
            }
        }
        notifyDataSetChanged()
    }
}
