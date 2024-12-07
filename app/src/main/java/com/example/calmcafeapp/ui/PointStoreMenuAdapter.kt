package com.example.calmcafeapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calmcafeapp.data.PointDiscount
import com.example.calmcafeapp.databinding.ItemRegPointstoremenuBinding

class PointStoreMenuAdapter(
    private var menus: List<PointDiscount>,
    private val onEditClick: (PointDiscount) -> Unit,
    private val onDiscountClick: (PointDiscount) -> Unit
    ) : RecyclerView.Adapter<PointStoreMenuAdapter.MenuViewHolder>() {

        class MenuViewHolder(private val binding: ItemRegPointstoremenuBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(menu: PointDiscount, onEditClick: (PointDiscount) -> Unit,  onDiscountClick: (PointDiscount) -> Unit) {
            binding.menuName.text = menu.name
            binding.menuDiscount.text = "${menu.pointDiscount}%"
            binding.editButton.setOnClickListener {
                onEditClick(menu)
            }
            binding.menuDiscount.setOnClickListener {
                onDiscountClick(menu)
            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
        val binding = ItemRegPointstoremenuBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return MenuViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
        holder.bind(menus[position], onEditClick, onDiscountClick)
    }

    override fun getItemCount(): Int = menus.size

    fun updateMenus(newPointDiscount: List<PointDiscount>) {
        menus = newPointDiscount
        notifyDataSetChanged()
    }
}
