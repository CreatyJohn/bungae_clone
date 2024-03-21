package com.softsquared.template.kotlin.src.main.category.big.middle

import android.content.Intent
import android.os.Bundle
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityBagManBinding
import com.softsquared.template.kotlin.src.main.goods.PushGoodsActivity

class BagManActivity : BaseActivity<ActivityBagManBinding>(ActivityBagManBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnGoBack.setOnClickListener { finish() }

        binding.btnKluch.setOnClickListener {
            prefs.setInt("SUBINT", 421)
            prefs.setString("SUBSTR", "클러치")

            finish()
        }

        binding.btnShorder.setOnClickListener {
            prefs.setInt("SUBINT", 422)
            prefs.setString("SUBSTR", "숄더백")

            finish()
        }

        binding.btnCross.setOnClickListener {
            prefs.setInt("SUBINT", 423)
            prefs.setString("SUBSTR", "크로스백")

            finish()
        }
    }
}