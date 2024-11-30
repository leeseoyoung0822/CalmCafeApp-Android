//package com.example.calmcafeapp.ui
//import android.view.LayoutInflater
//
//import android.view.ViewGroup
//import androidx.recyclerview.widget.RecyclerView
//import com.example.calmcafeapp.databinding.ItemMMenuBinding
//
//
//class M_MenuAdapter(
//    private val menuList: MutableList<MenuItem>,
//    private val onEditClick: (MenuItem) -> Unit,
//    private val onDeleteClick: (MenuItem) -> Unit
//) : RecyclerView.Adapter<M_MenuAdapter.MenuViewHolder>() {
//
//    inner class MenuViewHolder(private val binding: ItemMMenuBinding) : RecyclerView.ViewHolder(binding.root) {
//        fun bind(item: MenuItem) {
//            binding.menuName.text = item.name
//            binding.editButton.setOnClickListener { onEditClick(item) }
//            binding.deleteButton.setOnClickListener { onDeleteClick(item) }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MenuViewHolder {
//        val binding = ItemMMenuBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return MenuViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: MenuViewHolder, position: Int) {
//        holder.bind(menuList[position])
//    }
//
//    override fun getItemCount(): Int = menuList.size
//
//    fun addItem(item: MenuItem) {
//        menuList.add(item)
//        notifyItemInserted(menuList.size - 1)
//    }
//
//    fun removeItem(position: Int) {
//        menuList.removeAt(position)
//        notifyItemRemoved(position)
//    }
//}