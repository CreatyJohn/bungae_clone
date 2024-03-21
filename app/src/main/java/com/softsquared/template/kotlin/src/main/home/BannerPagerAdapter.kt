package com.softsquared.template.kotlin.src.main.home

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.softsquared.template.kotlin.R

class BannerPagerAdapter(private val sliderItems: MutableList<Int>, private val viewPager2: ViewPager2): RecyclerView.Adapter<BannerPagerAdapter.ViewPagerViewHolder>() {

    inner class ViewPagerViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val imageView = view.findViewById<ImageView>(R.id.slide_bv)

        fun onBind(image: Int) {
            imageView.setImageResource(image)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): BannerPagerAdapter.ViewPagerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.vpbanners_list_item, parent, false)
        return ViewPagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: BannerPagerAdapter.ViewPagerViewHolder, position: Int) {
        holder.onBind(sliderItems[position])
        if (position == sliderItems.size -1) {
            viewPager2.post(runnable)
        }
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    private val runnable = Runnable { sliderItems.addAll(sliderItems) }
}