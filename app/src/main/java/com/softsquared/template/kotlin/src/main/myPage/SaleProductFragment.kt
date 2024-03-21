package com.softsquared.template.kotlin.src.main.myPage

import android.annotation.SuppressLint
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.databinding.FragmentSaleProductBinding

class SaleProductFragment : BaseFragment<FragmentSaleProductBinding>(FragmentSaleProductBinding::bind, R.layout.fragment_sale_product) {

//    lateinit var recyclerView: RecyclerView
//    val datas : ArrayList<ReviewData> = ArrayList()

    private val sliderImageHandler: Handler = Handler()
    private val sliderImageRunnable = Runnable { binding.vpBanner2.currentItem = binding.vpBanner2.currentItem + 1 }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        //        for (i in 0 .. 1) {
//            datas.add(ReviewData(5, "할부로 거래해주셨습니다 좋은분이니 거래하는거 추천해드립니다", "거래용상점입니다", "1년", "피파온라인4"))
//            datas.add(ReviewData(4, "판매자님이 친절하셔서 기분 좋게 거래할 수 있었습니다. 감사합니다:)", "상점5481560호", "3년", "안드로이드스튜디오"))
//            datas.add(ReviewData(5, "친절하게 설명도 잘해 주셔서 기분 좋게 거래할 수 있었습니다! 감사합니다 :)", "nattramn", "3년", "자전거"))
//            datas.add(ReviewData(3, "설명도 상세하고 친절하게 잘해주셔서 좋은 거래였어요! 상점 번창하세요~~", "하늘공기계", "3년", "핸드폰"))
//        }
//
//        recyclerView = rootView.findViewById(R.id.rv_recommend_goods) as RecyclerView
//        recyclerView.layoutManager = LinearLayoutManager(requireContext())
//        recyclerView.adapter = ReviewRVAdapter(datas, requireContext())
//
//        rootView?.findViewById<TextView>(R.id.reviewtwo)?.text = datas.size.toString()
//
//        Log.e("RVSIZE", "${datas.size}")

        val imageList = arrayListOf<Int>().apply {
            for (i in 0..2) {
                add(R.drawable.other_banner_1)
                add(R.drawable.other_banner_2)
                add(R.drawable.other_banner_3)
            }
        }

        binding.vpBanner2.apply {
            adapter = OtherBannerPagerAdapter(imageList, binding.vpBanner2)
            offscreenPageLimit = 1
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    sliderImageHandler.removeCallbacks(sliderImageRunnable)
                    sliderImageHandler.postDelayed(sliderImageRunnable, 6000)
                }
            })
        }
    }

    override fun onResume() {
        super.onResume()
        sliderImageHandler.postDelayed(sliderImageRunnable, 1000)
    }

    override fun onPause() {
        super.onPause()
        sliderImageHandler.removeCallbacks(sliderImageRunnable)
    }

}