package com.softsquared.template.kotlin.src.main.goods

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageButton
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.softsquared.template.kotlin.R

class ImgRVAdapter(private val context: Context) : RecyclerView.Adapter<ImgRVAdapter.ViewHolder>() {

    var datas = mutableListOf<ImgData>()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgData: ImageView = itemView.findViewById(R.id.iv_post_goods)
        val imgDelete: ImageButton = itemView.findViewById(R.id.btn_delete_img)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_imgs,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataList = datas[position]
        holder.apply {
            Glide.with(context)
                .load(dataList.img)
                .override(65, 65)
                .into(imgData)
        }
        holder.imgDelete.setOnClickListener {
            removeItem(position)
        }
    }

    override fun getItemCount(): Int = datas.size

    var mPosition = 0

    private fun setPosition(position: Int) {
        mPosition = position
    }

    @SuppressLint("NotifyDataSetChanged")
    fun removeItem(position: Int) {
        if(position > 0) {
            datas.removeAt(position)
            notifyItemRemoved(position)
            notifyDataSetChanged()
        }
    }
}


class ImgGridRVAdapter(private val context: Context) : RecyclerView.Adapter<ImgGridRVAdapter.ViewHolder>() {

    var datas = mutableListOf<GallaryActivity.ImgGridData>()

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imgData: ImageView = itemView.findViewById(R.id.iv_post_goods3)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recycler_imgs_grid,parent,false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val dataList = datas[position]
        holder.apply {
            Glide.with(context)
                .load(dataList.imgGrid)
                .override(115, 115)
                .into(imgData)
        }
    }

    override fun getItemCount(): Int = datas.size
}