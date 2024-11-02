package com.example.calmcafeapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calmcafeapp.data.CafeData
import com.example.calmcafeapp.databinding.ItemCafeBinding

// 어댑터 클래스
class CafeAdapter(private val cafeList: List<CafeData>) : RecyclerView.Adapter<CafeAdapter.CafeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafeViewHolder {
        // View Binding 객체 생성
        val binding = ItemCafeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CafeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CafeViewHolder, position: Int) {
        val cafe = cafeList[position]
        holder.bind(cafe) // 뷰 홀더에 데이터 바인딩
    }

    override fun getItemCount(): Int = cafeList.size

    // ViewHolder 클래스
    class CafeViewHolder(private val binding: ItemCafeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cafe: CafeData) {
            binding.cafeName.text = cafe.cafeName
            binding.cafeImage.setImageResource(
                binding.root.context.resources.getIdentifier(cafe.cafeImage.toString(), "drawable", binding.root.context.packageName)
            )
        }
    }

}
