package com.example.calmcafeapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.calmcafeapp.data.CafeData
import com.example.calmcafeapp.databinding.RankingItemViewBinding
import com.example.calmcafeapp.R


class CafeRecyclerViewAdapter(private var mItem: List<CafeData>) : RecyclerView.Adapter<CafeRecyclerViewAdapter.CafeViewHolder>() {

    class CafeViewHolder(private val binding: RankingItemViewBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(cafeData:CafeData){
            //binding.dogPhotoImg.=dogData.dog_img
            binding.cafeNameTv.text=cafeData.cafe_name
            binding.cafeStateTv.text= cafeData.cafe_state

            // 좋아요 버튼 상태 초기화
            updateLikeButton(cafeData.isLiked)

            // 좋아요 버튼 클릭 이벤트 설정
            binding.likesBtn.setOnClickListener {
                cafeData.isLiked = !cafeData.isLiked // 좋아요 상태 반전
                updateLikeButton(cafeData.isLiked) // 좋아요 버튼 이미지 업데이트
            }
        }

        private fun updateLikeButton(isLiked:Boolean) {
            if (isLiked) {
                binding.likesBtn.setImageResource(R.drawable.heart_filled) // 채워진 하트 이미지로 변경
            } else {
                binding.likesBtn.setImageResource(R.drawable.heart_empty) // 빈 하트 이미지로 변경
            }
        }
    }


    //만들어진 뷰홀더 없을때 뷰홀더(레이아웃) 생성하는 함수
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafeRecyclerViewAdapter.CafeViewHolder {
        val binding=RankingItemViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CafeRecyclerViewAdapter.CafeViewHolder(binding)
    }

    override fun getItemCount(): Int = mItem.size

    override fun onBindViewHolder(holder: CafeViewHolder, position: Int) {
        holder.bind(mItem[position])
    }

    fun updateData(newData: List<CafeData>) {
        mItem = newData
        notifyDataSetChanged()
    }

}