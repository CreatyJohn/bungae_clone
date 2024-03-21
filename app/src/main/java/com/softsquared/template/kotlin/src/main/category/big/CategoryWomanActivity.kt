package com.softsquared.template.kotlin.src.main.category.big

import android.content.Intent
import android.os.Bundle
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityCategoryWomanBinding
import com.softsquared.template.kotlin.src.main.category.big.middle.ShoesWomanActivity
import com.softsquared.template.kotlin.src.main.category.big.middle.WomanCoatActivity
import com.softsquared.template.kotlin.src.main.category.big.middle.WomanPaddingActivity
import com.softsquared.template.kotlin.src.main.goods.PushGoodsActivity
import com.softsquared.template.kotlin.src.main.myPage.ProfileFragment

class CategoryWomanActivity : BaseActivity<ActivityCategoryWomanBinding>(ActivityCategoryWomanBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnGoBack.setOnClickListener { finish() }

        binding.btnPadding.setOnClickListener {
            prefs.setInt("MIDDLEINT",  110)
            prefs.setString("MIDDLESTR", "패딩/점퍼")

            finish()
            val intent = Intent(this, WomanPaddingActivity::class.java)
            startActivity(intent)
        }

        binding.btnCoat.setOnClickListener {
            prefs.setInt("MIDDLEINT", 120)
            prefs.setString("MIDDLESTR", "코트")

            finish()
            val intent = Intent(this, WomanCoatActivity::class.java)
            startActivity(intent)
        }

        binding.btnMantoman.setOnClickListener {
            prefs.setInt("MIDDLEINT", 130)
            prefs.setString("MIDDLESTR", "맨투맨")

            finish()
        }
    }
}