package com.softsquared.template.kotlin.src.main

import android.content.Intent
import android.os.Bundle
import android.widget.TextView
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityMenuBinding
import com.softsquared.template.kotlin.src.main.search.CategorySearchActivity
import com.softsquared.template.kotlin.src.main.search.ResultSearchActivity

class MenuActivity : BaseActivity<ActivityMenuBinding>(ActivityMenuBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnExit.setOnClickListener {
            finish()
        }

        pressCategories.invoke()
        pressOldSale.invoke()
    }

    /** 해당 카테고리를 누르면 누른 카테고리로 검색 됨 */
    val pressCategories : () -> Unit = {
        binding.btnCategoryBike.setOnClickListener {
            prefs.setString("SETSTR", binding.tvMotobike.text.toString())
            performSearch()
        }

        binding.btnCategorySnikerz.setOnClickListener {
            prefs.setString("SETSTR", binding.tvSnikerz.text.toString())
            performSearch()
        }

        binding.btnCategoryWatch.setOnClickListener {
            prefs.setString("SETSTR", binding.tvWatch.text.toString())
            performSearch()
        }

        binding.btnCategoryStarGoods.setOnClickListener {
            prefs.setString("SETSTR", binding.tvStargoods.text.toString())
            performSearch()
        }

        binding.btnCategoryCycler.setOnClickListener {
            prefs.setString("SETSTR", binding.tvCycler.text.toString())
            performSearch()
        }

        binding.btnCategoryRider.setOnClickListener {
            prefs.setString("SETSTR", binding.tvRider.text.toString())
            performSearch()
        }

        binding.btnCategoryFigure.setOnClickListener {
            prefs.setString("SETSTR", binding.tvFiguar.text.toString())
            performSearch()
        }

        binding.btnCategoryCamping.setOnClickListener {
            prefs.setString("SETSTR", binding.tvCamping.text.toString())
            performSearch()
        }

        binding.btnCategoryHealth.setOnClickListener {
            prefs.setString("SETSTR", binding.tvHealth.text.toString())
            performSearch()
        }

        binding.btnCategorySoccer.setOnClickListener {
            prefs.setString("SETSTR", binding.tvSoccer.text.toString())
            performSearch()
        }

        binding.btnCategoryNintendo.setOnClickListener {
            prefs.setString("SETSTR", binding.tvNintendo.text.toString())
            performSearch()
        }

        binding.btnCategoryDslr.setOnClickListener {
            prefs.setString("SETSTR", binding.tvDslr.text.toString())
            performSearch()
        }
    }

    /** 중고물품 추천 검색 */
    val pressOldSale : () -> Unit = {
        binding.btnCategoryWoman.setOnClickListener {
            val str = binding.tvCategoryWoman.text.toString()
            prefs.setString("CATEGORYSTR", str)
            prefs.setInt("CATEGORYINT", 100)
            finish()
            val intent = Intent(this, CategorySearchActivity::class.java)
            startActivity(intent)
        }

        binding.btnCategoryMan.setOnClickListener {
            val str = binding.tvCategoryMan.text.toString()
            prefs.setString("CATEGORYSTR", str)
            prefs.setInt("CATEGORYINT", 200)
            finish()
            val intent = Intent(this, CategorySearchActivity::class.java)
            startActivity(intent)
        }

        binding.btnCategoryShoes.setOnClickListener {
            val str = binding.tvCategoryShoes.text.toString()
            prefs.setString("CATEGORYSTR", str)
            prefs.setInt("CATEGORYINT", 300)
            finish()
            val intent = Intent(this, CategorySearchActivity::class.java)
            startActivity(intent)
        }

        binding.btnCategoryBag.setOnClickListener {
            val str = binding.tvCategoryBag.text.toString()
            prefs.setString("CATEGORYSTR", str)
            prefs.setInt("CATEGORYINT", 400)
            finish()
            val intent = Intent(this, CategorySearchActivity::class.java)
            startActivity(intent)
        }
    }

    private fun performSearch() {
        prefs.setString("SEARCH", prefs.getString("SETSTR", ""))
        val intent = Intent(this, ResultSearchActivity::class.java)
        startActivity(intent)
    }
}