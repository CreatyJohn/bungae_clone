package com.softsquared.template.kotlin.src.main.login

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.kakao.sdk.auth.LoginClient
import com.kakao.sdk.auth.model.OAuthToken
import com.kakao.sdk.common.model.AuthErrorCause
import com.kakao.sdk.user.UserApiClient
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityLoginBinding
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.myPage.ProfileFragment
import com.softsquared.template.kotlin.util.TAG

class LoginActivity : BaseActivity<ActivityLoginBinding>(ActivityLoginBinding::inflate) {

    /** toast's TAG */

    private val SPLASH_TIME_OUT:Long = 5000

    private val sliderImageHandler: Handler = Handler()
    private val sliderImageRunnable = Runnable { binding.vpBoungeInfo.currentItem = binding.vpBoungeInfo.currentItem + 1 }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val imageList = arrayListOf<Int>().apply {
            for (i in 0..3) {
                add(R.drawable.slide_one)
                add(R.drawable.slide_two)
                add(R.drawable.slide_three)
                add(R.drawable.slide_four)
            }
        }

        binding.btnKakaoLogin.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }

        binding.btnOtherLogin.setOnClickListener {
            startActivity(Intent(this, MiniPageActivity::class.java))
            overridePendingTransition(R.anim.vertical_enter, R.anim.none)
        }

        binding.vpBoungeInfo.apply {
            adapter = ViewPagerAdapter(imageList, binding.vpBoungeInfo)
            offscreenPageLimit = 1
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                @SuppressLint("UseCompatLoadingForDrawables")
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    /** Indicator */
                    binding.indicator0IvMain.setImageDrawable(getDrawable(R.drawable.shape_circle_gray))
                    binding.indicator1IvMain.setImageDrawable(getDrawable(R.drawable.shape_circle_gray))
                    binding.indicator2IvMain.setImageDrawable(getDrawable(R.drawable.shape_circle_gray))
                    binding.indicator3IvMain.setImageDrawable(getDrawable(R.drawable.shape_circle_gray))

                    /** 자동 및 유사 무한 루프 되는 포지션의 값이 4로 나눴을때 떨어지는 몫이 0~3인경우 각각의 인디케이터로 해당하게 됨 */
                    when(position % 4){
                        0 -> binding.indicator0IvMain.setImageDrawable(getDrawable(R.drawable.shape_circle_black))
                        1 -> binding.indicator1IvMain.setImageDrawable(getDrawable(R.drawable.shape_circle_black))
                        2 -> binding.indicator2IvMain.setImageDrawable(getDrawable(R.drawable.shape_circle_black))
                        3 -> binding.indicator3IvMain.setImageDrawable(getDrawable(R.drawable.shape_circle_black))
                    }

                    sliderImageHandler.removeCallbacks(sliderImageRunnable)
                    sliderImageHandler.postDelayed(sliderImageRunnable, SPLASH_TIME_OUT)
                }
            })
        }

//        val keyHash = Utility.getKeyHash(this)
//        Log.d("Hash", keyHash)


        val callback: (OAuthToken?, Throwable?) -> Unit = { token, error ->
            if (error != null) {
                when {
                    error.toString() == AuthErrorCause.AccessDenied.toString() -> {
                        Log.e(TAG, "접근이 거부 됨(동의 취소)")
                    }
                    error.toString() == AuthErrorCause.InvalidClient.toString() -> {
                        Log.e(TAG, "유효하지 않은 앱")
                    }
                    error.toString() == AuthErrorCause.InvalidGrant.toString() -> {
                        Log.e(TAG, "인증 수단이 유효하지 않아 인증할 수 없는 상태")
                    }
                    error.toString() == AuthErrorCause.InvalidRequest.toString() -> {
                        Log.e(TAG, "요청 파라미터 오류")
                    }
                    error.toString() == AuthErrorCause.InvalidScope.toString() -> {
                        Log.e(TAG, "유효하지 않은 scope ID")
                    }
                    error.toString() == AuthErrorCause.Misconfigured.toString() -> {
                        Log.e(TAG, "설정이 올바르지 않음(android key hash)")
                    }
                    error.toString() == AuthErrorCause.ServerError.toString() -> {
                        Log.e(TAG, "서버 내부 에러")
                    }
                    error.toString() == AuthErrorCause.Unauthorized.toString() -> {
                        Log.e(TAG, "앱이 요청 권한이 없음")
                    }
                    else -> { // Unknown
                        Log.e(TAG, "기타 에러")
                    }
                }
            }
            else if (token != null) {
                Toast.makeText(this, "카카오로 로그인", Toast.LENGTH_SHORT).show()
                Log.e(TAG, "카카오로 로그인 ${token.accessToken}")
                prefs.setString("ACCESSTOKEN", token.accessToken)
                prefs.setString("LOGINRESULT", "KAKAO")
//                postUsers.invoke()
                val intent = Intent(this, MainActivity::class.java)
                Intent(this, ProfileFragment::class.java).putExtra("SENDRESULT", "KAKAO")
                intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            }
        }

        // 사용자 정보 요청 (기본)
        UserApiClient.instance.me { user, error ->
            if (error != null) {
                Log.e(TAG, "사용자 정보 요청 실패", error)
            } else if (user != null) {
                Log.i(
                    TAG, "사용자 정보 요청 성공" +
                            "\n회원번호: ${user.id}" +
                            "\n이메일: ${user.kakaoAccount?.email}" +
                            "\n닉네임: ${user.kakaoAccount?.profile?.nickname}" +
                            "\n프로필사진: ${user.kakaoAccount?.profile?.thumbnailImageUrl}"
                )
                prefs.setString("USERNAME", "${user.kakaoAccount?.profile?.nickname}")
                prefs.setString("USEREMAIL", "${user.kakaoAccount?.email}")
            }
        }

        binding.btnKakaoLogin.setOnClickListener {
            if(LoginClient.instance.isKakaoTalkLoginAvailable(this)){
                LoginClient.instance.loginWithKakaoTalk(this, callback = callback)
            }else{
                LoginClient.instance.loginWithKakaoAccount(this, callback = callback)
            }
        }
    }

    override fun onResume() {
        super.onResume()
        sliderImageHandler.postDelayed(sliderImageRunnable, 1000)
    }

    override fun onPause() {
        super.onPause()
        sliderImageHandler.removeCallbacks(sliderImageRunnable)
    }

}