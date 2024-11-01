package com.example.calmcafeapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.calmcafeapp.data.PointMenuDetail
import com.example.calmcafeapp.databinding.ItemPointMenuBinding

class PointMenuAdapter(
    private var pointMenuList: List<PointMenuDetail>,
    private val onUseCouponClick: (String) -> Unit // 콜백 추가
) : RecyclerView.Adapter<PointMenuAdapter.PointMenuViewHolder>() {

    inner class PointMenuViewHolder(private val binding: ItemPointMenuBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(menu: PointMenuDetail) {
            binding.menuName.text = "${menu.name} ${menu.pointDiscount}% 쿠폰"
            binding.menuPrice.text = "${menu.pointPrice} p"

            // 이미지 로딩: URL이 있을 경우 로드, 없으면 비워 둠
            Glide.with(binding.root.context)
                .load(menu.image)
                .placeholder(null) // 로딩 중 표시할 이미지 없음
                .into(binding.menuImage)

            // 버튼 클릭 리스너 설정 (예: "사용" 기능)
            binding.useButton.setOnClickListener {
                onUseCouponClick(menu.name)

            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointMenuViewHolder {
        val binding = ItemPointMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PointMenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PointMenuViewHolder, position: Int) {
        holder.bind(pointMenuList[position])
    }

    override fun getItemCount(): Int = pointMenuList.size

    // 데이터 업데이트 함수
    fun updatePointMenuList(newList: List<PointMenuDetail>) {
        pointMenuList = newList
        notifyDataSetChanged()
    }
}