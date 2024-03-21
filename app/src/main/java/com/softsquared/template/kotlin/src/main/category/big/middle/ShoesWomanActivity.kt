package com.softsquared.template.kotlin.src.main.category.big.middle

import android.content.Intent
import android.os.Bundle
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityShoesWomanBinding
import com.softsquared.template.kotlin.src.main.goods.PushGoodsActivity

class ShoesWomanActivity : BaseActivity<ActivityShoesWomanBinding>(ActivityShoesWomanBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnGoBack.setOnClickListener { finish() }

        binding.btnFlat.setOnClickListener {
            prefs.setInt("SUBINT", 333)
            prefs.setString("SUBSTR", "단화/플랫슈즈")

            finish()
        }

        binding.btnSliper.setOnClickListener {
            prefs.setInt("SUBINT", 331)
            prefs.setString("SUBSTR", "샌들/슬리퍼")

            finish()
        }

        binding.btnLoaper.setOnClickListener {
            prefs.setInt("SUBINT", 332)
            prefs.setString("SUBSTR", "구두")

            finish()
        }
    }
}