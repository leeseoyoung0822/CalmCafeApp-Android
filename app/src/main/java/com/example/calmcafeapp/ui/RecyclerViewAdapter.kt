package com.example.calmcafeapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.calmcafeapp.data.CafeData
import com.example.calmcafeapp.databinding.RankingItemViewBinding
import com.example.calmcafeapp.R


class RecyclerViewAdapter : RecyclerView.Adapter<RecyclerViewAdapter.CafeViewHolder>() {

    var datalist = mutableListOf<CafeData>()//리사이클러뷰에서 사용할 데이터 미리 정의 -> 나중에 MainActivity등에서 datalist에 실제 데이터 추가

    class CafeViewHolder(private val binding: RankingItemViewBinding): RecyclerView.ViewHolder(binding.root) {

        // 좋아요 상태를 나타내는 변수
        private var isLiked = false

        fun bind(cafeData:CafeData){
            //binding.dogPhotoImg.=dogData.dog_img
            binding.cafeNameTv.text=cafeData.cafe_name
            binding.cafeStateTv.text= cafeData.cafe_state

            // 좋아요 버튼 클릭 이벤트 설정
            binding.likesBtn.setOnClickListener {
                isLiked = !isLiked // 좋아요 상태 토글
                updateLikeButton() // 좋아요 버튼 이미지 업데이트
            }
        }

        private fun updateLikeButton() {
            if (isLiked) {
                binding.likesBtn.setImageResource(R.drawable.heart_filled) // 채워진 하트 이미지로 변경
            } else {
                binding.likesBtn.setImageResource(R.drawable.heart_empty) // 빈 하트 이미지로 변경
            }
        }
    }


    //만들어진 뷰홀더 없을때 뷰홀더(레이아웃) 생성하는 함수
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerViewAdapter.CafeViewHolder {
        val binding=RankingItemViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return RecyclerViewAdapter.CafeViewHolder(binding)
    }

    override fun getItemCount(): Int =datalist.size

    //recyclerview가 viewholder를 가져와 데이터 연결할때 호출
    //적절한 데이터를 가져와서 그 데이터를 사용하여 뷰홀더의 레이아웃 채움
    override fun onBindViewHolder(holder: CafeViewHolder, position: Int) {
        holder.bind(datalist[position])
    }
}
