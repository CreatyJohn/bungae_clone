package com.softsquared.template.kotlin.src.main.category.big

import android.content.Intent
import android.os.Bundle
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityCategoryShoesBinding
import com.softsquared.template.kotlin.src.main.category.big.middle.ShoesManActivity
import com.softsquared.template.kotlin.src.main.category.big.middle.ShoesWomanActivity
import com.softsquared.template.kotlin.src.main.category.big.middle.WomanPaddingActivity
import com.softsquared.template.kotlin.src.main.goods.PushGoodsActivity

class CategoryShoesActivity : BaseActivity<ActivityCategoryShoesBinding>(ActivityCategoryShoesBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnGoBack.setOnClickListener { finish() }

        binding.btnManShoes.setOnClickListener {
            prefs.setInt("MIDDLEINT", 320)
            prefs.setString("MIDDLESTR", "남성용")

            finish()
            val intent = Intent(this, ShoesManActivity::class.java)
            startActivity(intent)
        }

        binding.btnWomanShoes.setOnClickListener {
            prefs.setInt("MIDDLEINT", 330)
            prefs.setString("MIDDLESTR", "여성용")

            finish()
            val intent = Intent(this, ShoesWomanActivity::class.java)
            startActivity(intent)
        }

        binding.btnSnikerz.setOnClickListener {
            prefs.setInt("MIDDLEINT", 310)
            prefs.setString("MIDDLESTR", "스니커즈")

            finish()
        }
    }
}