package com.softsquared.template.kotlin.src.main.category.big.middle

import android.content.Intent
import android.os.Bundle
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityManPaddingBinding
import com.softsquared.template.kotlin.src.main.goods.PushGoodsActivity

class ManPaddingActivity : BaseActivity<ActivityManPaddingBinding>(ActivityManPaddingBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnGoBack.setOnClickListener { finish() }

        binding.btnBlue.setOnClickListener {
            prefs.setInt("SUBINT", 213)
            prefs.setString("SUBSTR", "블루종/항공점퍼")

            finish()
        }

        binding.btnLongPadding.setOnClickListener {
            prefs.setInt("SUBINT", 211)
            prefs.setString("SUBSTR", "롱패딩")

            finish()
        }

        binding.btnShortPadding.setOnClickListener {
            prefs.setInt("SUBINT", 212)
            prefs.setString("SUBSTR", "숏패딩")

            finish()
        }

        binding.btnEtc.setOnClickListener {
            prefs.setInt("SUBINT", 214)
            prefs.setString("SUBSTR", "기타(패딩/점퍼)")

            finish()
        }
    }
}