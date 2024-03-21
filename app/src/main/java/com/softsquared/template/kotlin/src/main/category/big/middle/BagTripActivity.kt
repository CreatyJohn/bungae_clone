package com.softsquared.template.kotlin.src.main.category.big.middle

import android.content.Intent
import android.os.Bundle
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityBagTripBinding
import com.softsquared.template.kotlin.src.main.goods.PushGoodsActivity

class BagTripActivity : BaseActivity<ActivityBagTripBinding>(ActivityBagTripBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnGoBack.setOnClickListener { finish() }

        binding.btnTrip.setOnClickListener {
            prefs.setInt("SUBINT", 432)
            prefs.setString("SUBSTR", "기타(여행용)")

            finish()
        }

        binding.btnCarrier.setOnClickListener {
            prefs.setInt("SUBINT", 431)
            prefs.setString("SUBSTR", "캐리어")

            finish()
        }
    }
}