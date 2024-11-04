package com.example.calmcafeapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.calmcafeapp.data.RecommendCafe
import com.example.calmcafeapp.data.RecommendStoreResDto
import com.example.calmcafeapp.databinding.ItemRecommendCafeBinding


class RecommendCafeAdapter(private var list: ArrayList<RecommendCafe>):RecyclerView.Adapter<RecommendCafeAdapter.RecommendCafeHolder>() {

    interface MyItemClickListener {
        fun onItemClick(cafe: RecommendCafe)
    }
    private var myItemClickListener: MyItemClickListener? = null

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        myItemClickListener = itemClickListener
    }

    inner class RecommendCafeHolder(val binding: ItemRecommendCafeBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val img = binding.cafeImage
        val title = binding.cafeName
        val address = binding.cafeAddress
        val crowd = binding.cafeCongestion
    }


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): RecommendCafeHolder {
        return RecommendCafeHolder(
            ItemRecommendCafeBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: RecommendCafeAdapter.RecommendCafeHolder, position: Int) {
        val recommendcafe = list[position]
        Glide.with(holder.itemView.context)
            .load(recommendcafe.cafeimg)
            .into(holder.img)
        holder.title.text = recommendcafe.title
        holder.address.text = recommendcafe.address
        holder.crowd.text = recommendcafe.crowd
    }

    override fun getItemCount(): Int {
        return list.size
    }
}
//
//class RecommendCafeAdapter(private var recommendList: List<RecommendStoreResDto>) :
//    RecyclerView.Adapter<RecommendCafeAdapter.RecommendCafeViewHolder>() {
//
//    inner class RecommendCafeViewHolder(private val binding: ItemRecommendCafeBinding) : RecyclerView.ViewHolder(binding.root) {
//
//        fun bind(store: RecommendStoreResDto) {
//            binding.cafeName.text = store.name
//            binding.cafeAddress.text = formatAddress(store.address) // "구" 단위로 주소를 포맷
//            binding.cafeCongestion.text = "현재 혼잡도: ${store.storeCongestionLevel}"
//
//            // 이미지 로딩: URL이 있을 경우 로드, 없으면 비워 둠
//            Glide.with(binding.root.context)
//                .load(store.image)
//                .placeholder(null) // 로딩 중 표시할 이미지 없음
//                .into(binding.cafeImage)
//        }
//        // 주소를 "구" 단위까지만 잘라서 반환하는 함수
//        private fun formatAddress(address: String): String {
//            val guIndex = address.indexOf("구")
//            return if (guIndex != -1) {
//                address.substring(0, guIndex + 1) // "구"까지 포함해서 잘라냄
//            } else {
//                address // "구"가 없으면 전체 주소 반환
//            }
//        }
//    }
//
//    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendCafeViewHolder {
//        val binding = ItemRecommendCafeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
//        return RecommendCafeViewHolder(binding)
//    }
//
//    override fun onBindViewHolder(holder: RecommendCafeViewHolder, position: Int) {
//        holder.bind(recommendList[position])
//    }
//
//    override fun getItemCount(): Int = recommendList.size
//
//    // 데이터 업데이트 함수
//    fun updateRecommendList(newList: List<RecommendStoreResDto>) {
//        recommendList = newList
//        notifyDataSetChanged()
//    }
//}
