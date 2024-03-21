package com.softsquared.template.kotlin.src.main.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityStoreNameBinding
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.reqres.MyName
import com.softsquared.template.kotlin.src.main.reqres.ResultJWT
import com.softsquared.template.kotlin.src.main.reqres.ResultToken
import com.softsquared.template.kotlin.src.main.reqres.RetrofitClient
import com.softsquared.template.kotlin.util.PreferenceUtil
import com.softsquared.template.kotlin.util.TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoreNameActivity : BaseActivity<ActivityStoreNameBinding>(ActivityStoreNameBinding::inflate) {

    /** Api 불러오기 */
    private val PostTokenAPI = RetrofitClient.apiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val startMain = Intent(this, MainActivity::class.java)
        val startLogin = Intent(this, LoginActivity::class.java)

        binding.btnGoBack.setOnClickListener { finish() }

        /** 첫 로그인시 회원가입 화면 : 나만의 상점 이름 짓기 */
        val tokenResponse = MutableLiveData<ResultToken>()
        binding.btnFinalStoreName.setOnClickListener {
            val howtologin = prefs.getString("LOGINRESULT", "null")
            val mytoken = prefs.getString("ACCESSTOKEN", "null")

            when (howtologin) {
                "NAVER" -> {
                    val myname = MyName(binding.etStoreName.text.toString())
                    prefs.setString("USERNAME", binding.etStoreName.text.toString())

                    PostTokenAPI.postNaverToken(mytoken, myname).enqueue(object : Callback<ResultToken> {
                        override fun onResponse(call: Call<ResultToken>, response: Response<ResultToken>) {
                            tokenResponse.value = response.body()
                            // 회원 토큰 로그
                            Log.d(TAG, "${tokenResponse.value}")
                            
                            // 회원 IDX 로컬 저장
                            prefs.setInt("USERIDX", tokenResponse.value!!.result.userIdx)
                            prefs.setString("JWT", tokenResponse.value!!.result.jwt)
                            Log.i(TAG, "회원가입 USERIDX 불러오기 : ${prefs.getInt("USERIDX", 3014)}")
                            Log.i(TAG, "회원가입 JWT 불러오기 : ${prefs.getString("JWT", "null")}")

                            startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(startMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                            showCustomToast("네이버 회원가입 성공")
                            Log.e(TAG, "네이버 회원가입 성공")
                        }
                        override fun onFailure(call: Call<ResultToken>, t: Throwable) {
                            Log.d(TAG, "${tokenResponse.value}")
                            startLogin.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(startLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                            showCustomToast("네이버 회원가입 실패")
                            Log.e(TAG, "네이버 회원가입 실패")
                        }
                    })
                }
                else -> {
                    startLogin.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(startLogin.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                }
            }
        }
    }
}