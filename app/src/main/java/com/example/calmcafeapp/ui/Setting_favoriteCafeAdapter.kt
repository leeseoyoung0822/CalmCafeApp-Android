package com.example.calmcafeapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calmcafeapp.data.CafeData
import com.example.calmcafeapp.databinding.ItemFavoriteCafeBinding

// 어댑터 클래스
class Setting_favoriteCafeAdapter(private val cafeList: List<CafeData>) : RecyclerView.Adapter<Setting_favoriteCafeAdapter.CafeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafeViewHolder {
        // View Binding 객체 생성
        val binding = ItemFavoriteCafeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CafeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CafeViewHolder, position: Int) {
        val cafe = cafeList[position]
        holder.bind(cafe) // 뷰 홀더에 데이터 바인딩
    }

    override fun getItemCount(): Int = cafeList.size

    // ViewHolder 클래스
    class CafeViewHolder(private val binding: ItemFavoriteCafeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cafe: CafeData) {
            binding.cafeName.text = cafe.cafe_name
            binding.cafeImage.setImageResource(
                binding.root.context.resources.getIdentifier(cafe.cafe_img.toString(), "drawable", binding.root.context.packageName)
            )
        }
    }

}
