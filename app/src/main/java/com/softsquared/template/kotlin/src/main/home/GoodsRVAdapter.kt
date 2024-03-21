package com.softsquared.template.kotlin.src.main.home

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.media.Image
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.src.main.goods.ProductDetailActivity
import kotlinx.android.parcel.Parcelize
import okhttp3.internal.notify

class GoodsRVAdapter (private val context: Context) : RecyclerView.Adapter<GoodsRVAdapter.ViewHolder>() {

    var datas = mutableListOf<GoodsData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_recycler_grid3, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    interface OnItemClickListener{
        fun onItemClick(v:View, data: GoodsData, pos : Int)
    }
    private var listener : OnItemClickListener? = null
    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtPrice: TextView = itemView.findViewById(R.id.tv_rv_price)
        val txtGoodsTitle: TextView = itemView.findViewById(R.id.tv_rv_info)
        val imgProfile: ImageView = itemView.findViewById(R.id.img_rv_photo)
        val ivSafePay: ImageView = itemView.findViewById(R.id.iv_safe_pay)
        val isfav: ImageView = itemView.findViewById(R.id.iv_selector)

        @SuppressLint("SetTextI18n")
        fun bind(item: GoodsData) {
            txtPrice.text = item.price+"원"
            txtGoodsTitle.text = item.title

            Glide.with(context)
                .load(item.img)
                .into(imgProfile)

            when (item.safepay) {
                true -> ivSafePay.show()
                else -> ivSafePay.makeInvisible()
            }

            when (item.isFav) {
                true -> isfav.setImageResource(R.drawable.ic_heart_selected)
                else -> isfav.setImageResource(R.drawable.ic_heart)
            }

            val pos = adapterPosition
            if(pos!= RecyclerView.NO_POSITION) {
                itemView.setOnClickListener {
                    listener?.onItemClick(itemView, item, pos)
                }
            }
        }
    }

    fun View.hide() {
        this.visibility= View.GONE
    }
    fun View.show() {
        this.visibility= View.VISIBLE
    }
    fun View.makeInvisible() {
        this.visibility= View.INVISIBLE
    }
}

class SearchGoodsRVAdapter (private val context: Context) : RecyclerView.Adapter<SearchGoodsRVAdapter.ViewHolder>() {

    var datas = mutableListOf<GoodsData>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_recycler_grid3_for_search, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount(): Int = datas.size

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(datas[position])
    }

    interface OnItemClickListener{
        fun onItemClick(v:View, data: GoodsData, pos : Int)
    }
    private var listener : OnItemClickListener? = null
    fun setOnItemClickListener(listener : OnItemClickListener) {
        this.listener = listener
    }

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {

        val txtPrice: TextView = itemView.findViewById(R.id.tv_rv_price)
        val txtGoodsTitle: TextView = itemView.findViewById(R.id.tv_rv_info)
        val imgProfile: ImageView = itemView.findViewById(R.id.img_rv_photo)
        val ivSafePay: ImageView = itemView.findViewById(R.id.iv_safe_pay)
        val isfav: ImageView = itemView.findViewById(R.id.iv_selector)

        @SuppressLint("SetTextI18n")
        fun bind(item: GoodsData) {
            txtPrice.text = item.price+"원"
            txtGoodsTitle.text = item.title

            Glide.with(context)
                .load(item.img)
                .into(imgProfile)

            when (item.safepay) {
                true -> ivSafePay.show()
                else -> ivSafePay.makeInvisible()
            }

            when (item.isFav) {
                true -> isfav.setImageResource(R.drawable.ic_heart_selected)
                else -> isfav.setImageResource(R.drawable.ic_heart)
            }

            val pos = adapterPosition
            if(pos!= RecyclerView.NO_POSITION)
            {
                itemView.setOnClickListener {
                    listener?.onItemClick(itemView,item,pos)
                }
            }
        }
    }

    fun View.hide() {
        this.visibility= View.GONE
    }
    fun View.show() {
        this.visibility= View.VISIBLE
    }
    fun View.makeInvisible() {
        this.visibility= View.INVISIBLE
    }
}