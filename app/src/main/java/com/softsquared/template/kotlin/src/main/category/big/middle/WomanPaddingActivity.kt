package com.softsquared.template.kotlin.src.main.category.big.middle

import android.content.Intent
import android.os.Bundle
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityWomanPaddingBinding
import com.softsquared.template.kotlin.src.main.goods.PushGoodsActivity

class WomanPaddingActivity : BaseActivity<ActivityWomanPaddingBinding>(ActivityWomanPaddingBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnGoBack.setOnClickListener { finish() }

        binding.btnBlue.setOnClickListener {
            prefs.setInt("SUBINT", 113)
            prefs.setString("SUBSTR", "블루종/항공점퍼")

            finish()
        }

        binding.btnLongPadding.setOnClickListener {
            prefs.setInt("SUBINT", 111)
            prefs.setString("SUBSTR", "롱패딩")

            finish()
        }

        binding.btnShortPadding.setOnClickListener {
            prefs.setInt("SUBINT", 112)
            prefs.setString("SUBSTR", "숏패딩")

            finish()
        }

        binding.btnEtc.setOnClickListener {
            prefs.setInt("SUBINT", 114)
            prefs.setString("SUBSTR", "기타(패딩/점퍼)")

            finish()
        }
    }
}