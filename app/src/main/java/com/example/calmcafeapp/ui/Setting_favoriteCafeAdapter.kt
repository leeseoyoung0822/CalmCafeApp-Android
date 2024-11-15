package com.example.calmcafeapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calmcafeapp.data.CafeData
import com.example.calmcafeapp.databinding.ItemFavoriteCafeBinding

// 어댑터 클래스
class Setting_favoriteCafeAdapter(private var cafeList: MutableList<CafeData>) :
    RecyclerView.Adapter<Setting_favoriteCafeAdapter.ViewHolder>()  {

    inner class ViewHolder(private val binding: ItemFavoriteCafeBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(cafeData: CafeData) {
            binding.cafeImage.setImageResource(cafeData.cafe_img)
            binding.cafeName.text = cafeData.cafe_name
            binding.storeCongestion.text = cafeData.congestionLevel
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // View Binding 객체 생성
        val binding = ItemFavoriteCafeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(cafeList[position])
    }

    override fun getItemCount(): Int = cafeList.size

    // 새로운 데이터를 추가하는 메서드
    fun updateData(newData: List<CafeData>) {
        cafeList.clear()
        cafeList.addAll(newData)
        notifyDataSetChanged()
    }



}
