package com.example.calmcafeapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calmcafeapp.databinding.ItemCafeBinding

// 데이터 모델 클래스
data class Cafe(val name: String, val location: String, val status: String, val imageResId: Int)

// 어댑터 클래스
class CafeAdapter(private val cafeList: List<Cafe>) : RecyclerView.Adapter<CafeAdapter.CafeViewHolder>() {

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
        fun bind(cafe: Cafe) {
            binding.cafeName.text = cafe.name
            binding.cafeLocation.text = cafe.location
            binding.cafeStatus.text = cafe.status
            binding.cafeImage.setImageResource(cafe.imageResId) // 이미지 리소스 설정
        }
    }
}
