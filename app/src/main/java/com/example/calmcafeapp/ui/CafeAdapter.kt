package com.example.calmcafeapp.ui

import com.example.calmcafeapp.R
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

// 데이터 모델 클래스
data class Cafe(val name: String, val location: String, val status: String, val imageResId: Int)

// 어댑터 클래스
class CafeAdapter(private val cafeList: List<Cafe>) : RecyclerView.Adapter<CafeAdapter.CafeViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CafeViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_cafe, parent, false)
        return CafeViewHolder(view)
    }

    override fun onBindViewHolder(holder: CafeViewHolder, position: Int) {
        val cafe = cafeList[position]
        holder.bind(cafe)
    }

    override fun getItemCount(): Int = cafeList.size

    // ViewHolder 클래스
    class CafeViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(cafe: Cafe) {
            val cafeName = itemView.findViewById<TextView>(R.id.cafeName)
            val cafeLocation = itemView.findViewById<TextView>(R.id.cafeLocation)
            val cafeStatus = itemView.findViewById<TextView>(R.id.cafeStatus)
            val cafeImage = itemView.findViewById<ImageView>(R.id.cafeImage)

            cafeName.text = cafe.name
            cafeLocation.text = cafe.location
            cafeStatus.text = cafe.status
            cafeImage.setImageResource(cafe.imageResId)
        }
    }
}
