package com.example.calmcafeapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.calmcafeapp.data.CafeCouponData
import com.example.calmcafeapp.data.CafeMenuData
import com.example.calmcafeapp.data.RecommendCafe
import com.example.calmcafeapp.databinding.ItemCouponBinding


class CouponCafeAdapter(private var list: ArrayList<CafeCouponData>):RecyclerView.Adapter<CouponCafeAdapter.MenuCouponHolder>() {

    interface MyItemClickListener {
        fun onItemClick(menu: CafeCouponData)
    }
    private var myItemClickListener: MyItemClickListener? = null

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        myItemClickListener = itemClickListener
    }

    inner class MenuCouponHolder(val binding: ItemCouponBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val sale = binding.couponDiscountRate
        val type = binding.couponType
        val date = binding.couponTime

    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MenuCouponHolder {
        return MenuCouponHolder(
            ItemCouponBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CouponCafeAdapter.MenuCouponHolder, position: Int) {
        val coupon = list[position]
        holder.type.text = coupon.coupon_type
        holder.sale.text = coupon.sale
        holder.date.text = coupon.expiry_date

    }

    override fun getItemCount(): Int {
        return list.size
    }
}