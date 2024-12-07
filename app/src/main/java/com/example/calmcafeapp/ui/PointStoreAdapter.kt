package com.example.calmcafeapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calmcafeapp.data.PointDiscount
import com.example.calmcafeapp.databinding.ItemPointstoremenuBinding

class PointStoreAdapter(
    private var pointDiscount: List<PointDiscount>,
    private val onSelectionChanged: (List<PointDiscount>) -> Unit
) : RecyclerView.Adapter<PointStoreAdapter.PointStoreViewHolder>() {

    private val selectedPointMenu = mutableSetOf<PointDiscount>()

    inner class PointStoreViewHolder(private val binding: ItemPointstoremenuBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(pointDiscount: PointDiscount) {

            binding.menuName.text = pointDiscount.name
            binding.menuDiscount.text = "  ${pointDiscount.pointDiscount.toString()}% 할인"
            binding.editButton.visibility = View.GONE

            // 체크박스 상태 설정
            // 체크박스 상태 설정
            binding.checkbox.isChecked = selectedPointMenu.contains(pointDiscount)
            binding.checkbox.visibility = View.VISIBLE

            // 아이템 클릭 시 체크박스 토글
            binding.root.setOnClickListener {
                toggleSelection(pointDiscount)
            }

            // 체크박스 상태 변경 리스너
            binding.checkbox.setOnCheckedChangeListener { _, isChecked ->
                if (isChecked) {
                    selectedPointMenu.add(pointDiscount)
                } else {
                    selectedPointMenu.remove(pointDiscount)
                }
                onSelectionChanged(selectedPointMenu.toList())
            }
        }

        private fun toggleSelection(pointDiscount: PointDiscount) {
            if (selectedPointMenu.contains(pointDiscount)) {
                selectedPointMenu.remove(pointDiscount)
            } else {
                selectedPointMenu.add(pointDiscount)
            }
            onSelectionChanged(selectedPointMenu.toList())
            notifyItemChanged(adapterPosition)
        }


    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PointStoreViewHolder {
        val binding =
            ItemPointstoremenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return PointStoreViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PointStoreViewHolder, position: Int) {
        holder.bind(pointDiscount[position])
    }

    override fun getItemCount(): Int = pointDiscount.size

    fun updateData(newPointDiscount: List<PointDiscount>) {
        pointDiscount = newPointDiscount
        selectedPointMenu.clear() // 기존 선택 상태 초기화
        notifyDataSetChanged()
    }
}
