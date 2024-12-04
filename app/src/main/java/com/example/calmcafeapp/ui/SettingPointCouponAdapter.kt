package com.example.calmcafeapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calmcafeapp.data.PointCoupon
import com.example.calmcafeapp.data.PointMenuDetailResDto
import com.example.calmcafeapp.databinding.ItemPointCouponListBinding

class SettingPointCouponAdapter (
    private var couponList: List<PointCoupon>,
    private val onItemClick: (PointCoupon) -> Unit
    ) : RecyclerView.Adapter<SettingPointCouponAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(coupon: PointCoupon)
    }

    private var itemClickListener: OnItemClickListener? = null
    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }

    inner class ViewHolder(private val binding: ItemPointCouponListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(coupon: PointCoupon) {
            binding.cafeName.text = "${coupon.storeName}"
            binding.cafeMenu.text = "${coupon.menuName} "
            binding.expiration.text = "${coupon.expirationStart} ~ ${coupon.expirationEnd}"
            binding.discount.text = "${coupon.discount}% "

            binding.root.setOnClickListener {
                onItemClick(coupon) // PointMenuDetailResDto를 클릭 리스너에 전달
            }
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