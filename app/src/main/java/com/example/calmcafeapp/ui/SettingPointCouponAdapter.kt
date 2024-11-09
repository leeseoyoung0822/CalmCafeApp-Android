package com.example.calmcafeapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calmcafeapp.data.PointCoupon
import com.example.calmcafeapp.databinding.ItemPointCouponListBinding
import com.example.calmcafeapp.databinding.ItemPointMenuBinding

class SettingPointCouponAdapter (private var couponList: List<PointCoupon>) :
    RecyclerView.Adapter<SettingPointCouponAdapter.ViewHolder>() {

    inner class ViewHolder(private val binding: ItemPointCouponListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(coupon: PointCoupon) {
            binding.cafeName.text = coupon.storeName
            binding.cafeMenu.text = "coupon.menuName ${coupon.discount}% 할인"
            binding.expiration.text = "${coupon.expirationStart} ~ ${coupon.expirationEnd}"
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemPointCouponListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(couponList[position])
    }

    override fun getItemCount(): Int = couponList.size

    fun updateData(newCoupons: List<PointCoupon>) {
        couponList = newCoupons
        notifyDataSetChanged()
    }
}