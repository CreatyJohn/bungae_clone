package com.softsquared.template.kotlin.src.main

import android.content.Intent
import android.os.Bundle
import android.util.Log
import com.google.firebase.messaging.FirebaseMessaging
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityMainBinding
import com.softsquared.template.kotlin.src.main.home.HomeFragment
import com.softsquared.template.kotlin.src.main.chat.ChatFragment
import com.softsquared.template.kotlin.src.main.goods.PushGoodsActivity
import com.softsquared.template.kotlin.src.main.myPage.ProfileFragment
import com.softsquared.template.kotlin.src.main.search.SearchActivity
import com.softsquared.template.kotlin.util.TAG

class MainActivity : BaseActivity<ActivityMainBinding>(ActivityMainBinding::inflate) {


    private fun initFirebase() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (task.isSuccessful) {
                Log.e(TAG, "initFirebase: ${task.result}")
                // eRglclxDQN-WJXBo4HAcr4:APA91bGno98-2r-kqxMqndmLLAKKGRLtd-VKZXNjOHpQiiD8oN_sUNWyOf802pgZ5Jqc2ZOSHC_4B3eyF_57nBbvDKcjIZPuzZbKOfDbgAPgyDMWCzuHxG3QM9zcOhtg9qwN2VjZ-A6G
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportFragmentManager.beginTransaction().replace(R.id.main_frm, HomeFragment()).commitAllowingStateLoss()

        val pushgoods = Intent(this, PushGoodsActivity::class.java)
        val searchgoods = Intent(this, SearchActivity::class.java)

        binding.mainBtmNav.run {
            setOnItemSelectedListener { item ->
                when (item.itemId) {
                    R.id.menu_main_btm_nav_home -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.main_frm, HomeFragment())
                            .commitAllowingStateLoss()
                    }
                    R.id.menu_main_btm_nav_search -> {
                        startActivity(searchgoods)
                    }
                    R.id.menu_main_btm_nav_regist -> {
                        startActivity(pushgoods)
                    }
                    R.id.menu_main_btm_nav_chat -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.main_frm, ChatFragment())
                            .commitAllowingStateLoss()
                    }
                    R.id.menu_main_btm_nav_my_page -> {
                        supportFragmentManager.beginTransaction()
                            .replace(R.id.main_frm, ProfileFragment())
                            .commitAllowingStateLoss()
                    }
                }
                true
            }
            selectedItemId = R.id.menu_main_btm_nav_home
        }
    }
}