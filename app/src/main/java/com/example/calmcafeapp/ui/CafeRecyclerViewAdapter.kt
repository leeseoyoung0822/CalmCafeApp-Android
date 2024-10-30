package com.example.calmcafeapp.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.calmcafeapp.databinding.RankingItemViewBinding
import com.example.calmcafeapp.R
import com.example.calmcafeapp.data.StoreRanking


class CafeRecyclerViewAdapter(private var mItem: MutableList<StoreRanking>,
                              private val onItemClick: (storeId: Int) -> Unit,
                              private val onFavoriteClick: (storeId: Int, isFavorite: Boolean) -> Unit
) : RecyclerView.Adapter<CafeRecyclerViewAdapter.CafeViewHolder>() {

    class CafeViewHolder(private val binding: RankingItemViewBinding): RecyclerView.ViewHolder(binding.root) {

        fun bind(
             storeRanking: StoreRanking,
             position: Int,
             onItemClick: (storeId: Int) -> Unit,
             onFavoriteClick: (storeId: Int, isFavorite: Boolean) -> Unit
        ) {
            Glide.with(binding.cafePhotoImv.context)
                .load(storeRanking.image)
                .into(binding.cafePhotoImv) // 서버에서 가져온 이미지 로딩
            binding.cafeNameTv.text = storeRanking.name
            binding.cafeStateTv.text = storeRanking.userCongestionLevel

            // 아이템 클릭 이벤트 설정
            itemView.setOnClickListener {
                onItemClick(storeRanking.id)  // 클릭 시 storeId를 전달
            }
            // 좋아요 버튼 클릭 이벤트
            binding.likesBtn.setOnClickListener {
                onFavoriteClick(storeRanking.id, storeRanking.isFavorite) // 클릭 시 storeId를 전달하여 ViewModel에서 API 호출
                storeRanking.isFavorite = !storeRanking.isFavorite // 좋아요 상태 반전
                updateLikeButton(storeRanking.isFavorite) // 버튼 이미지 업데이트
            }

            // 상위 3개의 아이템에만 순위 배지를 표시
            if (position < 3) {
                binding.rankingBadge.visibility = View.VISIBLE
                binding.rankingBadge.text = (position + 1).toString()
            } else {
                binding.rankingBadge.visibility = View.GONE
            }

            // 초기 좋아요 버튼 이미지 설정
            updateLikeButton(storeRanking.isFavorite) // 좋아요 버튼 이미지 업데이트
        }

        private fun updateLikeButton(isLiked:Boolean) {
            if (isLiked) {
                binding.likesBtn.setImageResource(R.drawable.heart_filled) // 채워진 하트 이미지로 변경
            } else {
                binding.likesBtn.setImageResource(R.drawable.heart_empty) // 빈 하트 이미지로 변경
            }
        }
    }
    // 특정 ID의 즐겨찾기 상태를 업데이트
    fun updateLikeStatus(storeId: Int?) {
        storeId?.let { id ->
            val index = mItem.indexOfFirst { it.id == id }
            if (index != -1) {
                mItem[index].isFavorite = !mItem[index].isFavorite // 상태 반전
                notifyItemChanged(index)
            }
        }
    }

    // 뷰홀더 없을때 뷰홀더(레이아웃) 생성하는 함수
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafeRecyclerViewAdapter.CafeViewHolder {
        val binding = RankingItemViewBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return CafeViewHolder(binding)
    }

    override fun getItemCount(): Int = mItem.size

    override fun onBindViewHolder(holder: CafeViewHolder, position: Int) {
        holder.bind(mItem[position], position, onItemClick, onFavoriteClick)
    }

    fun updateData(newCafeList: List<StoreRanking>) {
        mItem.clear()
        mItem.addAll(newCafeList)
        notifyDataSetChanged()
    }
}