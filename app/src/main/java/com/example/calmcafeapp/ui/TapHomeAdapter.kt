package com.example.calmcafeapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.calmcafeapp.data.MenuDetail
import com.example.calmcafeapp.databinding.ItemMenuCardBinding


class TapHomeAdapter(
    private var menuList: List<MenuDetail>,
) : RecyclerView.Adapter<TapHomeAdapter.MenuViewHolder>() {

    inner class MenuViewHolder(private val binding: ItemMenuCardBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(menu: MenuDetail) {
            // 바인딩 객체를 통해 아이템 데이터 설정


            // 이미지 로딩 (Glide 사용)
            Glide.with(binding.root.context)
                .load(menu.image)
                .placeholder(null)
                .into(binding.menuImage)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = ItemMenuCardBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(menuList[position])
    }

    override fun getItemCount(): Int = menuList.size
}