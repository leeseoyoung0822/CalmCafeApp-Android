package com.example.calmcafeapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calmcafeapp.data.PromotionDetailResDto
import com.example.calmcafeapp.databinding.ItemCoupon1Binding

class CouponCafeAdapter(private var list: ArrayList<PromotionDetailResDto>) :
    RecyclerView.Adapter<CouponCafeAdapter.MenuCouponHolder>() {

    interface MyItemClickListener {
        fun onItemClick(menu: PromotionDetailResDto)
    }

    private var myItemClickListener: MyItemClickListener? = null

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        myItemClickListener = itemClickListener
    }

    inner class MenuCouponHolder(val binding: ItemCoupon1Binding) :
        RecyclerView.ViewHolder(binding.root) {
        val type = binding.couponType
        val date = binding.couponTime

        init {
            binding.root.setOnClickListener {
                myItemClickListener?.onItemClick(list[adapterPosition])
            }
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MenuCouponHolder {
        return MenuCouponHolder(
            ItemCoupon1Binding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: CouponCafeAdapter.MenuCouponHolder, position: Int) {
        val coupon = list[position]

        // 시간을 두 자리 형식으로 변환
        val startTime = coupon.startTime
        val endTime = coupon.endTime

        // 데이터 설정
        holder.type.text = coupon.promotionType
        holder.date.text = "$startTime ~ $endTime"
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
