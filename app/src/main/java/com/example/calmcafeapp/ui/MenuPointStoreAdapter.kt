package com.example.calmcafeapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.calmcafeapp.data.CafeMenuData
import com.example.calmcafeapp.data.MenuDetailResDto
import com.example.calmcafeapp.data.PointMenuDetailResDto
import com.example.calmcafeapp.data.RecommendCafe
import com.example.calmcafeapp.databinding.ItemMenuCafeBinding
import com.example.calmcafeapp.databinding.ItemMenuPointstoreBinding
import com.example.calmcafeapp.databinding.ItemRecommendCafeBinding

class MenuPointStoreAdapter(private var list: ArrayList<PointMenuDetailResDto>):RecyclerView.Adapter<MenuPointStoreAdapter.MenuCafeHolder>() {

    interface MyItemClickListener {
        fun onItemClick(menu: PointMenuDetailResDto)
    }
    private var myItemClickListener: MyItemClickListener? = null

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        myItemClickListener = itemClickListener
    }

    inner class MenuCafeHolder(val binding: ItemMenuPointstoreBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val img = binding.menuImage
        val menu = binding.menuName
        val price = binding.menuPrice


    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MenuCafeHolder {
        return MenuCafeHolder(
            ItemMenuPointstoreBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MenuCafeHolder, position: Int) {
        val menu = list[position]
        Glide.with(holder.itemView.context)
            .load(menu.image)
            .into(holder.img)
        holder.menu.text = "(${menu.pointDiscount}%할인) ${menu.name}"
        holder.price.text = "${menu.pointPrice.toString()}P"

    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun updateData(newData: List<PointMenuDetailResDto>) {
        list.clear()
        list.addAll(newData)
        notifyDataSetChanged()
    }
}