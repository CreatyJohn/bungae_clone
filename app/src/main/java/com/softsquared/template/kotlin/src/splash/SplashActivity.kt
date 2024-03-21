package com.softsquared.template.kotlin.src.splash

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import com.bumptech.glide.Glide
import com.kakao.sdk.user.UserApiClient
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivitySplashBinding
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.login.LoginActivity
import com.softsquared.template.kotlin.util.PreferenceUtil
import com.softsquared.template.kotlin.util.TAG

@SuppressLint("CustomSplashScreen")
class SplashActivity : BaseActivity<ActivitySplashBinding>(ActivitySplashBinding::inflate){

    lateinit var intentT: Intent

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

//        prefs.destroyData()

        // Actionbar 제거
        supportActionBar?.hide()

        Glide.with(this).load(R.raw.splash_lightning).into(binding.ivGifBounge)

        Log.d(TAG, "로그인 방법 : ${prefs.getString("LOGINRESULT", "null")}")
        Log.d(TAG, "액세스 토큰: ${prefs.getString("ACCESSTOKEN", "null")}")
        Log.d(TAG, "사용자 이름 : ${prefs.getString("USERNAME", "null")}")

        val loginResult = prefs.getString("LOGINRESULT", "null")


        // 로그인 정보 확인 : 자동로그인
        when(loginResult) {
            "NAVER" -> {
                intentT = Intent(this, MainActivity::class.java)
            }
            "KAKAO" -> {
                UserApiClient.instance.accessTokenInfo { tokenInfo, error ->
                    if (error != null) {
                        Log.e(TAG, "토큰 정보 보기 실패 $error")
                        intentT = Intent(this, LoginActivity::class.java)
                    }
                    else if (tokenInfo != null) {
                        Log.e(TAG, "토큰 정보 보기 성공 $tokenInfo")
                        intentT = Intent(this, MainActivity::class.java)
                    }
                }
            }
            "MYPHONE" -> {
                intentT = Intent(this, MainActivity::class.java)
            }
            else -> {
                Log.e(TAG, "토큰 정보 보기 실패  ${prefs.getString("LOGINRESULT", "null")}")
                intentT = Intent(this, LoginActivity::class.java)
            }
        }

        val handler = Handler(Looper.getMainLooper())
        handler.postDelayed(Runnable {
            intentT.apply {
                startActivity(this)
                finish()
                if (loginResult != "null") {
                    showCustomToast("자동 로그인")
                }
                overridePendingTransition(R.anim.none, R.anim.none)
            }
        }, 1000)
    }
}