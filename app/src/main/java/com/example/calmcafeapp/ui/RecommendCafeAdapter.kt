import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.calmcafeapp.data.RecommendStoreResDto
import com.example.calmcafeapp.databinding.ItemRecommendCafeBinding

class RecommendCafeAdapter(
    private var list: List<RecommendStoreResDto>
) : RecyclerView.Adapter<RecommendCafeAdapter.RecommendCafeHolder>() {

    interface MyItemClickListener {
        fun onItemClick(cafe: RecommendStoreResDto)
    }

    private var myItemClickListener: MyItemClickListener? = null

    fun setMyItemClickListener(itemClickListener: MyItemClickListener) {
        myItemClickListener = itemClickListener
    }

    inner class RecommendCafeHolder(private val binding: ItemRecommendCafeBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(cafe: RecommendStoreResDto) {
            // 축약 주소 사용
            val shortAddress = getShortAddress(cafe.address)
            // 데이터 바인딩
            Glide.with(binding.cafeImage.context)
                .load(cafe.image)
                .into(binding.cafeImage)

            binding.cafeName.text = cafe.name
            binding.cafeAddress.text = shortAddress
            binding.cafeCongestion.text = when (cafe.storeCongestionLevel) {
                "CALM" -> "현재 혼잡도: 한산"
                "NORMAL" -> "현재 혼잡도: 보통"
                "BUSY" -> "현재 혼잡도: 혼잡"
                else -> "현재 혼잡도: 정보 없음"
            }

            // 클릭 리스너
            binding.root.setOnClickListener {
                myItemClickListener?.onItemClick(cafe)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecommendCafeHolder {
        val binding = ItemRecommendCafeBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return RecommendCafeHolder(binding)
    }

    override fun onBindViewHolder(holder: RecommendCafeHolder, position: Int) {
        holder.bind(list[position])
    }

    override fun getItemCount(): Int = list.size

    // 데이터를 갱신하는 메서드
    fun updateData(newData: List<RecommendStoreResDto>) {
        list = newData
        notifyDataSetChanged()
    }

    private fun getShortAddress(fullAddress: String): String {
        val parts = fullAddress.split(" ")
        return when {
            parts.size >= 3 -> "${parts[0]} ${parts[1]} ${parts[2]}" // 시/도 + 구/군 + 동
            parts.size >= 2 -> "${parts[0]} ${parts[1]}"            // 시/도 + 구/군
            else -> fullAddress                                     // 주소가 짧은 경우 그대로 반환
        }
    }
}
