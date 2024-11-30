package com.example.calmcafeapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import androidx.recyclerview.widget.RecyclerView
import com.example.calmcafeapp.data.Promotion
import com.example.calmcafeapp.databinding.ItemCouponBinding
class PromotionAdapter(
    private var promotions: List<Promotion>,
    private val onSelectionChanged: (List<Promotion>) -> Unit
) : RecyclerView.Adapter<PromotionAdapter.PromotionViewHolder>() {

    private val selectedPromotions = mutableSetOf<Promotion>()

    inner class PromotionViewHolder(private val binding: ItemCouponBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(promotion: Promotion) {
            val formattedStartTime = formatTime(promotion.startTime)
            val formattedEndTime = formatTime(promotion.endTime)

            binding.couponType.text = "${promotion.promotionType} ${promotion.discount}%"
            binding.couponTime.text = "$formattedStartTime - $formattedEndTime"

            // 체크박스 상태 설정
            binding.checkbox.isChecked = selectedPromotions.contains(promotion)
            binding.checkbox.visibility = View.VISIBLE

            // 아이템 클릭 시 체크박스 토글
            binding.root.setOnClickListener {
                toggleSelection(promotion)
            }

            // 체크박스 상태 변경 리스너
            binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedPromotions.add(promotion)
                } else {
                    selectedPromotions.remove(promotion)
                }
                onSelectionChanged(selectedPromotions.toList())
            }
        }

        private fun toggleSelection(promotion: Promotion) {
            if (selectedPromotions.contains(promotion)) {
                selectedPromotions.remove(promotion)
            } else {
                selectedPromotions.add(promotion)
            }
            onSelectionChanged(selectedPromotions.toList())
            notifyItemChanged(adapterPosition)
        }

        private fun formatTime(time: String): String {
            return time.substringBeforeLast(":") // 초 단위 제거
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PromotionViewHolder {
        val binding =
            ItemCouponBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PromotionViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PromotionViewHolder, position: Int) {
        holder.bind(promotions[position])
    }

    override fun getItemCount(): Int = promotions.size

    fun updateData(newPromotions: List<Promotion>) {
        promotions = newPromotions
        selectedPromotions.clear() // 기존 선택 상태 초기화
        notifyDataSetChanged()
    }
}
