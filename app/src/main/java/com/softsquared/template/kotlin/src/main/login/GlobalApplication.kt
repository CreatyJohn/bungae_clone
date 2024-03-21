package com.softsquared.template.kotlin.src.main.login

import android.app.Application
import com.kakao.sdk.common.KakaoSdk

class GlobalApplication : Application() {
    override fun onCreate() {
        super.onCreate()

        KakaoSdk.init(this, "7ece0ebd6e9c15e826d4f7509bc94791")
    }
}