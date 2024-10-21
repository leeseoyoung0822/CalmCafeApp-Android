package com.example.calmcafeapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.calmcafeapp.data.RecommendCafe
import com.example.calmcafeapp.databinding.ItemRecommendCafeBinding

class RecommendCafeAdapter(private var list: ArrayList<RecommendCafe>):RecyclerView.Adapter<RecommendCafeAdapter.RecommendCafeHolder>() {

    interface MyItemClickListener {
        fun onItemClick(cafe: RecommendCafe)
    }
    private var myItemClickListener: MyItemClickListener? = null

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        myItemClickListener = itemClickListener
    }

    inner class RecommendCafeHolder(val binding: ItemRecommendCafeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val img = binding.cafeImage
        val title = binding.cafeName
        val address = binding.cafeLocation
        val crowd = binding.cafeCrowd
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecommendCafeHolder {
        return RecommendCafeHolder(
            ItemRecommendCafeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecommendCafeAdapter.RecommendCafeHolder, position: Int) {
        val recommendcafe = list[position]
        Glide.with(holder.itemView.context)
            .load(recommendcafe.cafeimg)
            .into(holder.img)
        holder.title.text = recommendcafe.title
        holder.address.text = recommendcafe.address
        holder.crowd.text = recommendcafe.crowd
    }

    override fun getItemCount(): Int {
        return list.size
    }
}