package com.softsquared.template.kotlin.src.main.login

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityPhoneCompanyBinding


class PhoneCompanyActivity : BaseActivity<ActivityPhoneCompanyBinding>(ActivityPhoneCompanyBinding::inflate) {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.flBackground.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.none, R.anim.vertical_exit)
        }

        binding.radioGroup.setOnCheckedChangeListener { group, checkedId ->
            when(checkedId) {
                R.id.btn_SKT -> {
                    val name = binding.btnSKT.text.toString()
                    val returnIntent = Intent()
                    returnIntent.putExtra("PHONE_DATA", name)
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }
                R.id.btn_KT -> {
                    val name = binding.btnKT.text.toString()
                    val returnIntent = Intent()
                    returnIntent.putExtra("PHONE_DATA", name)
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }
                R.id.btn_LG -> {
                    val name = binding.btnLG.text.toString()
                    val returnIntent = Intent()
                    returnIntent.putExtra("PHONE_DATA", name)
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }
                R.id.btn_SKT_other -> {
                    val name = binding.btnSKTOther.text.toString()
                    val returnIntent = Intent()
                    returnIntent.putExtra("PHONE_DATA", name)
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }
                R.id.btn_KT_other -> {
                    val name = binding.btnKTOther.text.toString()
                    val returnIntent = Intent()
                    returnIntent.putExtra("PHONE_DATA", name)
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }
                R.id.btn_LG_other -> {
                    val name = binding.btnLGOther.text.toString()
                    val returnIntent = Intent()
                    returnIntent.putExtra("PHONE_DATA", name)
                    setResult(Activity.RESULT_OK, returnIntent)
                    finish()
                }
            }
        }
    }
}