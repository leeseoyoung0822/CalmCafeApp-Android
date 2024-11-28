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

    private val subwayLineColors = mapOf(
        "수도권 1호선" to Color.parseColor("#0052A4"),
        "수도권 2호선" to Color.parseColor("#009D3E"),
        "수도권 3호선" to Color.parseColor("#EF7C1C"),
        "수도권 4호선" to Color.parseColor("#00A5DE"),
        "수도권 5호선" to Color.parseColor("#996CAC"),
        "수도권 6호선" to Color.parseColor("#CD7C2F"),
        "수도권 7호선" to Color.parseColor("#747F00"),
        "수도권 8호선" to Color.parseColor("#E6186C"),
        "수도권 9호선" to Color.parseColor("#BDB092"),
        "경의중앙선" to Color.parseColor("#77C4A3"),
        "공항철도" to Color.parseColor("#0090D2"),
        "분당선" to Color.parseColor("#F5A200"),
        "신분당선" to Color.parseColor("#D4003B"),
        "경춘선" to Color.parseColor("#0C8E72")
    )

    private val subwayLineIcons = mapOf(
        "수도권 1호선" to R.drawable.ic_subway_line1,
        "수도권 2호선" to R.drawable.ic_subway_line2,
        "수도권 3호선" to R.drawable.ic_subway_line3,
        "수도권 4호선" to R.drawable.ic_subway_line4,
        "수도권 5호선" to R.drawable.ic_subway_line5,
        "수도권 6호선" to R.drawable.ic_subway_line6,
        "수도권 7호선" to R.drawable.ic_subway_line7,
        "수도권 8호선" to R.drawable.ic_subway_line8,
        "수도권 9호선" to R.drawable.ic_subway_line9,
        "경의중앙선" to R.drawable.ic_subway_gyeongui,
        "공항철도" to R.drawable.ic_subway_airport,
        "분당선" to R.drawable.ic_subway_bundang,
        "신분당선" to R.drawable.ic_subway_sinbundang,
        "경춘선" to R.drawable.ic_subway_gyeongchun
    )


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
            binding.ivTransportIcon.setImageResource(R.drawable.walking)
        }
    }

    inner class TransitViewHolder(private val binding: ItemTransitBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(subPath: SubPath) {
            val lane = subPath.lane?.firstOrNull()
            val transportNumber = lane?.busNo ?: lane?.name ?: ""
            val sectionTime = subPath.sectionTime ?: 0
            val stationCount = subPath.stationCount ?: 0

            // 교통 수단 및 번호 설정
            if (subPath.trafficType == 1) { // 지하철
                val lineName = lane?.name ?: "알 수 없음"
                binding.tvTransitType.text = "$lineName"
                binding.tvTransitDetails.text = "${stationCount}개 역 이동, ${sectionTime}분"

                // 색상 설정
                val lineColor = subwayLineColors[lineName] ?: Color.GRAY // 기본값은 회색
                binding.viewLine.setBackgroundColor(lineColor)

                // 아이콘 설정
                val iconRes = subwayLineIcons[lineName] ?: R.drawable.subway // 기본 아이콘
                binding.ivTransportIcon.setImageResource(iconRes)
            } else if (subPath.trafficType == 2) { // 버스
                binding.tvTransitType.text = "버스\n${transportNumber}번"
                binding.tvTransitDetails.text = "${stationCount}정거장, ${sectionTime}분"

                binding.ivTransportIcon.setImageResource(R.drawable.ic_bus)
                binding.viewLine.setBackgroundColor(Color.BLUE) // 버스는 기본 파란색
            }
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
