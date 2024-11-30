package com.example.calmcafeapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.calmcafeapp.data.Menu
import com.example.calmcafeapp.data.StoreDetailResponse
import com.example.calmcafeapp.databinding.ItemMMenuBinding
class M_MenuAdapter(
    private var menus: List<Menu>,
    private val onEditClick: (Menu) -> Unit,
    private val onDeleteClick: (Menu) -> Unit
) : RecyclerView.Adapter<M_MenuAdapter.MenuViewHolder>() {

    class MenuViewHolder(private val binding: ItemMMenuBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(menu: Menu, onEditClick: (Menu) -> Unit, onDeleteClick: (Menu) -> Unit) {
            binding.menuName.text = menu.name
            Glide.with(binding.menuImage.context).load(menu.image).into(binding.menuImage)

            binding.editButton.setOnClickListener {
                onEditClick(menu)
            }
            binding.deleteButton.setOnClickListener {
                onDeleteClick(menu)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = ItemMMenuBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(menus[position], onEditClick, onDeleteClick)
    }

    override fun getItemCount(): Int = menus.size

    fun updateMenus(newMenus: List<Menu>) {
        menus = newMenus
        notifyDataSetChanged()
    }
}
