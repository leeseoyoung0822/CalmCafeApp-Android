package com.example.calmcafeapp.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.calmcafeapp.data.CafeData
import com.example.calmcafeapp.databinding.RankingItemViewBinding
import com.example.calmcafeapp.R


class CafeRecyclerViewAdapter(private var mItem: List<CafeData>) : RecyclerView.Adapter<CafeRecyclerViewAdapter.CafeViewHolder>(),Filterable {

    var datalist = mutableListOf<CafeData>()//리사이클러뷰에서 사용할 데이터 미리 정의 -> 나중에 MainActivity등에서 datalist에 실제 데이터 추가

    // 필터링된 데이터 리스트 (초기값은 원본 리스트)
    private var filteredList: List<CafeData> = mItem


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

    override fun getItemCount(): Int = filteredList.size

    //recyclerview가 viewholder를 가져와 데이터 연결할때 호출
    //적절한 데이터를 가져와서 그 데이터를 사용하여 뷰홀더의 레이아웃 채움
    override fun onBindViewHolder(holder: CafeViewHolder, position: Int) {
        holder.bind(filteredList[position])
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            // 필터링 작업을 수행하는 함수
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString().trim()

                filteredList = if (charString.isBlank()) {
                    mItem // 아무 입력도 없으면 원래 리스트 보여줌
                } else {
                    // 필터링 조건에 맞는 항목만 리스트로 반환
                    mItem.filter { cafeData ->
                        cafeData.cafe_name.contains(charString, true) // 카페 이름에 검색어 포함 여부 확인
                    }
                }

                val filterResults = FilterResults().apply {
                    values = filteredList
                }
                return filterResults
            }

            // 필터링된 결과를 화면에  반영
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filteredList = results?.values as List<CafeData>
                notifyDataSetChanged()
            }
        }
    }
    // Fragment에서 호출될 검색 함수
    fun performSearch(query: String) {
        filter.filter(query)
    }

}