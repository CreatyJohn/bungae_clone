package com.softsquared.template.kotlin.src.main.myPage

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.lifecycle.MutableLiveData
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.kakao.sdk.user.UserApiClient
import com.navercorp.nid.NaverIdLoginSDK
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.login.LoginActivity
import com.softsquared.template.kotlin.src.main.reqres.JustResponse
import com.softsquared.template.kotlin.src.main.reqres.RetrofitClient
import com.softsquared.template.kotlin.src.splash.SplashActivity
import com.softsquared.template.kotlin.util.PreferenceUtil
import retrofit2.Call
import retrofit2.Response

@Suppress("UNREACHABLE_CODE", "CAST_NEVER_SUCCEEDS")
class BottomSheetDialogSetting : BottomSheetDialogFragment() {

    private val TAG = "BUNGGE"

    private lateinit var mainActivity: MainActivity

    /** Api 불러오기 */
    private val UserDataAPI = RetrofitClient.apiService

    companion object {
        lateinit var prefs: PreferenceUtil
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)
        val view = inflater.inflate(R.layout.bottomsheetlayout, container, false)

        view?.findViewById<Button>(R.id.btn_setting)?.text = "로그아웃"
        view?.findViewById<Button>(R.id.btn_client)?.text = "회원탈퇴"

        return view
    }

    @Deprecated("Deprecated in Java")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        prefs = PreferenceUtil(mainActivity.applicationContext)

        view?.findViewById<Button>(R.id.btn_parcel)?.setOnClickListener {
            dismiss()
        }
        view?.findViewById<Button>(R.id.btn_client)?.setOnClickListener {
            val loginresult = prefs.getString("LOGINRESULT", "null")
            Log.e(TAG, loginresult)

            /** 회원탈퇴 */
            when (loginresult) {
                "KAKAO" -> {
                    kakaoUserDelete.invoke()
//                    deleteUser.invoke() //추후에 카카오 로그인이 구현된다면 사용
                    prefs.destroyData()
                }
                "NAVER" -> {
                    startSplashActivity.invoke()
                    mainActivity.showCustomToast("회원탈퇴 완료")
                    deleteUser.invoke()
                    prefs.destroyData()
                }
                "MYPHONE" -> {
                    startSplashActivity.invoke()
                    mainActivity.showCustomToast("회원탈퇴 완료")
                    prefs.destroyData()
                }
                else -> { mainActivity.showCustomToast("회원정보 조회 오류") }
            }
        }
        view?.findViewById<Button>(R.id.btn_receller)?.setOnClickListener {
            dismiss()
        }
        view?.findViewById<Button>(R.id.btn_setting)?.setOnClickListener {

            val loginresult = prefs.getString("LOGINRESULT", "null")
            Log.e(TAG, loginresult)

            /** 로그인 및 로그아웃 */
            when (loginresult) {
                "KAKAO" -> {
                    kakaoUserLogout.invoke()
                    prefs.destroyData()
                }
                "NAVER" -> {
                    startSplashActivity.invoke()
                    mainActivity.showCustomToast("네이버 로그아웃")
                    prefs.destroyData()
                }
                "MYPHONE" -> {
                    startSplashActivity.invoke()
                    mainActivity.showCustomToast("본인인증 로그아웃")
                    prefs.destroyData()
                }
                else -> { mainActivity.showCustomToast("회원정보 조회 오류") }
            }
        }
    }

    /** 회원탈퇴 API */
    val deleteUser : () -> Unit = {

        mainActivity.showLoadingDialog(requireContext())

        val userDelete = MutableLiveData<JustResponse>()
        var userIdx = prefs.getInt("USERIDX", 0)
        val jwt = prefs.getString("JWT","null")

        UserDataAPI.deleteUserData(jwt, userIdx)
            .enqueue(object : retrofit2.Callback<JustResponse> {
                override fun onResponse(call: Call<JustResponse>, response: Response<JustResponse>) {
                    userDelete.value = response.body()

                    Log.d(com.softsquared.template.kotlin.util.TAG, "요청코드 : ${userDelete.value!!.code}")
                    Log.d(com.softsquared.template.kotlin.util.TAG, "요청코드 : ${userDelete.value!!.message}")
                }
                override fun onFailure(call: Call<JustResponse>, t: Throwable) {
                    t.printStackTrace()

                    Log.d(com.softsquared.template.kotlin.util.TAG, "요청코드 : ${userDelete.value!!.code}")
                    Log.d(com.softsquared.template.kotlin.util.TAG, "요청코드 : ${userDelete.value!!.message}")
                }
            })

        mainActivity.dismissLoadingDialog()
    }

    val startSplashActivity : () -> Unit = {
        activity?.let{
            val intent = Intent(context, SplashActivity::class.java)
            startActivity(intent)
        }
    }

    val kakaoUserLogout : () -> Unit = {
        UserApiClient.instance.logout { error ->
            if (error != null) {
                mainActivity.showCustomToast("카카오 로그아웃 실패 $error")
            }else {
                mainActivity.showCustomToast("카카오 로그아웃")
            }
            activity?.let {
                val intent = Intent(context, LoginActivity::class.java)
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            }
            mainActivity.finish()
        }
    }

    val kakaoUserDelete : () -> Unit = {
//            NaverIdLoginSDK.logout()
        UserApiClient.instance.unlink { error ->
            if (error != null) {
                mainActivity.showCustomToast("카카오 회원탈퇴 실패 $error")
            }else {
                mainActivity.showCustomToast("카카오 회원탈퇴")
                activity?.let {
                    val intent = Intent(context, LoginActivity::class.java)
                    startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                }
                mainActivity.finish()
            }
        }
    }
}