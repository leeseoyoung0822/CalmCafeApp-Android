package com.example.calmcafeapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calmcafeapp.databinding.ItemCouponBinding

data class Coupon(
    val storeName: String,
    val description: String,
    val startDate: String,
    val endDate: String
)

class CouponAdapter(private val couponList: List<Coupon>) : RecyclerView.Adapter<CouponAdapter.CouponViewHolder>() {

    inner class CouponViewHolder(val binding: ItemCouponBinding) : RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CouponViewHolder {
        val binding = ItemCouponBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return CouponViewHolder(binding)
    }

    override fun onBindViewHolder(holder: CouponViewHolder, position: Int) {
        val coupon = couponList[position]
        holder.binding.tvStoreName.text = coupon.storeName // ID를 올바르게 매칭
        holder.binding.tvCouponDescription.text = coupon.description
        holder.binding.tvValidityPeriod.text = "유효 기간: ${coupon.startDate} ~ ${coupon.endDate}"
    }

    override fun getItemCount(): Int = couponList.size
}
