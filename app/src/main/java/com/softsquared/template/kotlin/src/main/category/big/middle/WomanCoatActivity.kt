package com.softsquared.template.kotlin.src.main.category.big.middle

import android.content.Intent
import android.os.Bundle
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityWomanCoatBinding
import com.softsquared.template.kotlin.src.main.goods.PushGoodsActivity

class WomanCoatActivity : BaseActivity<ActivityWomanCoatBinding>(ActivityWomanCoatBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnGoBack.setOnClickListener { finish() }

        binding.btnSpringCoat.setOnClickListener {
            prefs.setInt("SUBINT", 122)
            prefs.setString("SUBSTR", "봄/가을 코트")

            finish()
        }

        binding.btnWinterCoat.setOnClickListener {
            prefs.setInt("SUBINT", 121)
            prefs.setString("SUBSTR", "겨울 코트")

            finish()
        }
    }
}