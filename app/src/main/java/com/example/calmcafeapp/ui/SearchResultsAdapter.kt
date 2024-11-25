package com.example.calmcafeapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.calmcafeapp.data.SearchStoreResDto
import com.example.calmcafeapp.databinding.ItemSearchResultBinding

class SearchResultsAdapter(
    private var searchResults: List<SearchStoreResDto>,
    private val itemClickListener: (SearchStoreResDto) -> Unit
) : RecyclerView.Adapter<SearchResultsAdapter.SearchResultViewHolder>() {

    inner class SearchResultViewHolder(private val binding: ItemSearchResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: SearchStoreResDto) {
            binding.tvName.text = item.name
            binding.tvAddress.text = item.address
            Glide.with(binding.root.context)
                .load(item.image)
                .into(binding.ivImage)

            binding.root.setOnClickListener {
                itemClickListener(item)
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

    fun updateData(newData: List<SearchStoreResDto>) {
        searchResults = newData
        notifyDataSetChanged()
    }
}