package com.softsquared.template.kotlin.src.main.category.big

import android.content.Intent
import android.os.Bundle
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityCategoryBagBinding
import com.softsquared.template.kotlin.src.main.category.big.middle.BagManActivity
import com.softsquared.template.kotlin.src.main.category.big.middle.BagTripActivity
import com.softsquared.template.kotlin.src.main.category.big.middle.BagWomanActivity
import com.softsquared.template.kotlin.src.main.category.big.middle.ShoesManActivity
import com.softsquared.template.kotlin.src.main.goods.PushGoodsActivity

class CategoryBagActivity : BaseActivity<ActivityCategoryBagBinding>(ActivityCategoryBagBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnGoBack.setOnClickListener { finish() }

        binding.btnWomanBag.setOnClickListener {
            prefs.setInt("MIDDLEINT", 410)
            prefs.setString("MIDDLESTR", "여성가방")

            finish()
            val intent = Intent(this, BagWomanActivity::class.java)
            startActivity(intent)
        }
        
        binding.btnManBag.setOnClickListener {
            prefs.setInt("MIDDLEINT", 420)
            prefs.setString("MIDDLESTR", "남성가방")

            finish()
            val intent = Intent(this, BagManActivity::class.java)
            startActivity(intent)
        }

        binding.btnTrip.setOnClickListener {
            prefs.setInt("MIDDLEINT", 430)
            prefs.setString("MIDDLESTR", "여행용")

            finish()
            val intent = Intent(this, BagTripActivity::class.java)
            startActivity(intent)
        }
    }
}