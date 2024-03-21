package com.softsquared.template.kotlin.src.main.myPage

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.src.main.home.GoodsData

class ReviewRVAdapter(private val context: Context) : RecyclerView.Adapter<ReviewRVAdapter.ViewHolder>() {

    var datas = mutableListOf<ReviewData>()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        private val txtScore: TextView = itemView.findViewById(R.id.tv_score)
        private val txtWrite: TextView = itemView.findViewById(R.id.tv_info)
        private val txtBuyer: TextView = itemView.findViewById(R.id.tv_buyer)
        private val txtDate: TextView = itemView.findViewById(R.id.tv_date)
        private val txtCategory: TextView = itemView.findViewById(R.id.tv_category)

        @SuppressLint("SetTextI18n")
        fun bind(item: ReviewData) {
            txtScore.text = item.score.toString()
            txtWrite.text = item.write
            txtBuyer.text = item.buyer
            txtDate.text = item.date+" 전"
            txtCategory.text = item.category
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view =
            LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_reviews, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

}