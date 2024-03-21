package com.softsquared.template.kotlin.src.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.viewpager.widget.ViewPager
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityNoticeBinding
import com.softsquared.template.kotlin.src.main.myPage.WishListActivity
import com.softsquared.template.kotlin.src.main.myPage.excitedgoods.ExcitedGoodsPagerAdapter
import com.softsquared.template.kotlin.src.main.myPage.excitedgoods.SeenGoodsFragment
import com.softsquared.template.kotlin.src.main.myPage.excitedgoods.WantGoodsFragment

class NoticeActivity : BaseActivity<ActivityNoticeBinding>(ActivityNoticeBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.hide()

        /** 탭 레이아웃 + 뷰페이저 https://onlyfor-me-blog.tistory.com/295 */
        val adapter = ExcitedGoodsPagerAdapter(supportFragmentManager)
        adapter.addFragment(WantGoodsFragment(), "새소식")
        adapter.addFragment(SeenGoodsFragment(), "키워드 알림")
        binding.vpExciteGoods.adapter = adapter
        binding.tlTwotabs.setupWithViewPager(binding.vpExciteGoods)

        binding.btnGoBack.setOnClickListener { finish() }

        /** 페이지가 넘어갈때마다 있는 감시 리스너 */
        binding.vpExciteGoods.addOnPageChangeListener(object :
            ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                when (position) {
                    0 -> { //탭레이아웃 포지션 얻기 0 이 Tab 1
                        Log.e(com.softsquared.template.kotlin.util.TAG, "새소식")
                        binding.btnEdit.hide()
                    }
                    1 -> {
                        Log.e(com.softsquared.template.kotlin.util.TAG, "키워드 알림")
                        binding.btnEdit.show()
                    }
                }
            }
            override fun onPageSelected(position: Int) {
            }
            override fun onPageScrollStateChanged(state: Int) {
            }
        })
    }
}