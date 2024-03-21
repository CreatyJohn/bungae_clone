package com.softsquared.template.kotlin.src.main.category.big.middle

import android.content.Intent
import android.os.Bundle
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityShoesManBinding
import com.softsquared.template.kotlin.src.main.goods.PushGoodsActivity

class ShoesManActivity : BaseActivity<ActivityShoesManBinding>(ActivityShoesManBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnGoBack.setOnClickListener { finish() }

        binding.btnLoaper.setOnClickListener {
            prefs.setInt("SUBINT", 322)
            prefs.setString("SUBSTR", "구두/로퍼")

            finish()
        }

        binding.btnSliper.setOnClickListener {
            prefs.setInt("SUBINT", 321)
            prefs.setString("SUBSTR", "샌들/슬리퍼")

            finish()
        }

        binding.btnWalker.setOnClickListener {
            prefs.setInt("SUBINT", 323)
            prefs.setString("SUBSTR", "워커/부츠")

            finish()
        }
    }
}