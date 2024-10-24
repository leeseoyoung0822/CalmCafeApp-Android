package com.example.calmcafeapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.calmcafeapp.data.CafeMenuData
import com.example.calmcafeapp.data.RecommendCafe
import com.example.calmcafeapp.databinding.ItemMenuCafeBinding
import com.example.calmcafeapp.databinding.ItemRecommendCafeBinding

class MenuCafeAdapter(private var list: ArrayList<CafeMenuData>):RecyclerView.Adapter<MenuCafeAdapter.MenuCafeHolder>() {

    interface MyItemClickListener {
        fun onItemClick(menu: CafeMenuData)
    }
    private var myItemClickListener: MyItemClickListener? = null

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        myItemClickListener = itemClickListener
    }

    inner class MenuCafeHolder(val binding: ItemMenuCafeBinding) :
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
            ItemMenuCafeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MenuCafeHolder, position: Int) {
        val menu = list[position]
        Glide.with(holder.itemView.context)
            .load(menu.menu_img)
            .into(holder.img)
        holder.menu.text = menu.menu_name
        holder.price.text = menu.price

    }

    override fun getItemCount(): Int {
        return list.size
    }
}