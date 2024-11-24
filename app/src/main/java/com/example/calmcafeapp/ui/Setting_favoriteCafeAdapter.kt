package com.example.calmcafeapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.calmcafeapp.R
import com.example.calmcafeapp.data.CafeData
import com.example.calmcafeapp.data.FavoriteStore
import com.example.calmcafeapp.databinding.ItemFavoriteCafeBinding

// 어댑터 클래스
class Setting_favoriteCafeAdapter(
    private var cafeList: MutableList<FavoriteStore>,
    private val onFavoriteClick: (storeId: Int) -> Unit
) : RecyclerView.Adapter<Setting_favoriteCafeAdapter.ViewHolder>()  {

    inner class ViewHolder(private val binding: ItemFavoriteCafeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cafe: FavoriteStore) {
            // 이미지 로드 (Glide 사용)
            Glide.with(binding.cafeImage.context)
                .load(cafe.image)
                .into(binding.cafeImage)

            binding.cafeName.text = cafe.name
            binding.storeCongestion.text = "현재 혼잡도: ${cafe.storeCongestionLevel}"

            // 즐겨찾기 버튼 클릭 이벤트
            binding.favoriteBtn.setOnClickListener {
                onFavoriteClick(cafe.id) // 클릭된 매장의 ID 전달
                removeItem(adapterPosition) // 항목 삭제
            }
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
    fun updateData(newData: List<FavoriteStore>) {
        cafeList.clear()
        cafeList.addAll(newData)
        notifyDataSetChanged()
    }
    // 항목 삭제
    private fun removeItem(position: Int) {
        if (position in cafeList.indices) {
            cafeList.removeAt(position)
            notifyItemRemoved(position)
        }
    }
}
