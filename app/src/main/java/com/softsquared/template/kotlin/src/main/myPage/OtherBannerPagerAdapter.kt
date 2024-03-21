package com.softsquared.template.kotlin.src.main.myPage

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.softsquared.template.kotlin.R

class OtherBannerPagerAdapter(

    private val sliderItems: MutableList<Int>,
    private val viewPager2: ViewPager2
): RecyclerView.Adapter<OtherBannerPagerAdapter.ViewPagerViewHolder>() {

    inner class ViewPagerViewHolder(view: View): RecyclerView.ViewHolder(view) {
        private val imageView = view.findViewById<ImageView>(R.id.slide_bv2)

        fun onBind(image: Int) {
            imageView.setImageResource(image)
        }
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): OtherBannerPagerAdapter.ViewPagerViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.vpbanners2_list_item, parent, false)
        return ViewPagerViewHolder(view)
    }

    override fun onBindViewHolder(holder: OtherBannerPagerAdapter.ViewPagerViewHolder, position: Int) {
        holder.onBind(sliderItems[position])
        if (position == sliderItems.size-1) {
            viewPager2.post(runnable)
        }
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    private val runnable = Runnable { sliderItems.addAll(sliderItems) }
}