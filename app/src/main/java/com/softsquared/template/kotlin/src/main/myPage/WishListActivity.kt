package com.softsquared.template.kotlin.src.main.myPage

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.ActionBar
import androidx.viewpager.widget.ViewPager
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityWishListBinding
import com.softsquared.template.kotlin.src.main.myPage.excitedgoods.ExcitedGoodsPagerAdapter
import com.softsquared.template.kotlin.src.main.myPage.excitedgoods.SeenGoodsFragment
import com.softsquared.template.kotlin.src.main.myPage.excitedgoods.WantGoodsFragment

class WishListActivity : BaseActivity<ActivityWishListBinding>(ActivityWishListBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val actionBar: ActionBar? = supportActionBar
        actionBar?.hide()

        /** 탭 레이아웃 + 뷰페이저 https://onlyfor-me-blog.tistory.com/295 */
        val adapter = ExcitedGoodsPagerAdapter(supportFragmentManager)
        adapter.addFragment(WantGoodsFragment(), "찜")
        adapter.addFragment(SeenGoodsFragment(), "최근 본 상품")
        binding.vpExciteGoods.adapter = adapter
        binding.tlTwotabs.setupWithViewPager(binding.vpExciteGoods)

        binding.btnGoBack.setOnClickListener { finish() }

        /** 페이지가 넘어갈때마다 있는 감시 리스너 */
        binding.vpExciteGoods.addOnPageChangeListener(object :
            ViewPager.OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                when (position) {
                    0 -> { //탭레이아웃 포지션 얻기 0 이 Tab 1
                        Log.e(com.softsquared.template.kotlin.util.TAG, "찜")
                        binding.btnAdd.show()
                        binding.btnFilter.show()
                        binding.btnFilter.setImageResource(R.drawable.ic_filter)
                    }
                    1 -> {
                        Log.e(com.softsquared.template.kotlin.util.TAG, "최근 본 상품")
                        binding.btnAdd.hide()
                        binding.btnFilter.setImageResource(R.drawable.icons8_edit_24)
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