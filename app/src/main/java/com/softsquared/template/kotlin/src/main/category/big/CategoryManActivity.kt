package com.softsquared.template.kotlin.src.main.category.big

import android.content.Intent
import android.os.Bundle
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityCategoryManBinding
import com.softsquared.template.kotlin.src.main.category.big.middle.BagWomanActivity
import com.softsquared.template.kotlin.src.main.category.big.middle.ManCoatActivity
import com.softsquared.template.kotlin.src.main.category.big.middle.ManPaddingActivity
import com.softsquared.template.kotlin.src.main.goods.PushGoodsActivity

class CategoryManActivity : BaseActivity<ActivityCategoryManBinding>(ActivityCategoryManBinding::inflate)  {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnGoBack.setOnClickListener { finish() }
        
        binding.btnPadding.setOnClickListener {
            prefs.setInt("MIDDLEINT", 210)
            prefs.setString("MIDDLESTR", "패딩/점퍼")

            finish()
            val intent = Intent(this, ManPaddingActivity::class.java)
            startActivity(intent)
        }

        binding.btnCoat.setOnClickListener {
            prefs.setInt("MIDDLEINT", 220)
            prefs.setString("MIDDLESTR", "코트")

            finish()
            val intent = Intent(this, ManCoatActivity::class.java)
            startActivity(intent)
        }

        binding.btnMantoman.setOnClickListener {
            prefs.setInt("MIDDLEINT", 230)
            prefs.setString("MIDDLESTR", "맨투맨")

            finish()
        }
    }
}