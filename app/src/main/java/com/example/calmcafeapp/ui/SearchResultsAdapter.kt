package com.example.calmcafeapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.calmcafeapp.data.SearchResult
import com.example.calmcafeapp.data.SearchStoreResDto
import com.example.calmcafeapp.databinding.ItemSearchResultBinding


class SearchResultsAdapter(
    private var searchResults: List<SearchStoreResDto>,
    private var latitude: Double = 0.0,
    private var longitude: Double = 0.0,
    private val onItemClick: (name: String, storeId: Int, address: String, congestionLevel: String,latitude: Double, longitude: Double) -> Unit
) : RecyclerView.Adapter<SearchResultsAdapter.SearchResultViewHolder>() {

    // ViewHolder 정의
    inner class SearchResultViewHolder(private val binding: ItemSearchResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SearchStoreResDto) {
            binding.tvName.text = item.name
            binding.tvAddress.text = item.address
            Glide.with(binding.root.context).load(item.image).into(binding.ivImage)

            itemView.setOnClickListener {
                onItemClick(item.name, item.id, item.address, item.congestionLevel, item.latitude, item.longitude) // 클릭 시 storeId와 좌표 전달
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchResultViewHolder {
        val binding = ItemSearchResultBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SearchResultViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SearchResultViewHolder, position: Int) {
        holder.bind(searchResults[position])
    }

    override fun getItemCount(): Int = searchResults.size

    // 데이터 업데이트 함수
    fun updateData(newData: List<SearchStoreResDto>) {
        searchResults = newData
        notifyDataSetChanged()
    }
}

