package com.softsquared.template.kotlin.src.main.login

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.facebook.*
import com.facebook.login.LoginManager
import com.navercorp.nid.NaverIdLoginSDK
import com.navercorp.nid.oauth.NidOAuthLogin
import com.navercorp.nid.oauth.OAuthLoginCallback
import com.navercorp.nid.profile.NidProfileCallback
import com.navercorp.nid.profile.data.NidProfileResponse
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityMiniPageBinding
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.reqres.ResultJWT
import com.softsquared.template.kotlin.src.main.reqres.ResultToken
import com.softsquared.template.kotlin.src.main.reqres.RetrofitClient
import com.softsquared.template.kotlin.util.PreferenceUtil
import com.softsquared.template.kotlin.util.TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.util.*

@Suppress("DEPRECATED_IDENTITY_EQUALS")
class MiniPageActivity : BaseActivity<ActivityMiniPageBinding>(ActivityMiniPageBinding::inflate) {

    /** Api 불러오기 */
    private val PostTokenAPI = RetrofitClient.apiService

    /** 네이버 로그인 */
    private var email: String = ""
    private var gender: String = ""
    private var name: String = ""
    private var image: String = ""
    private var phone: String = ""

    /** 페이스북 로그인 */
    private lateinit var callbackManager: CallbackManager
    private lateinit var loginManager: LoginManager

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        /** startactivity intent */
        val startMain = Intent(this, MainActivity::class.java)
        val startNext = Intent(this, StoreNameActivity::class.java)

        // Naver-Login
        binding.run {
            binding.btnNaverLogin.setOnClickListener {
                val oAuthLoginCallback = object : OAuthLoginCallback {
                    // 네이버 로그인 API 호출 성공 시 유저 정보를 가져온다
                    override fun onSuccess() {
                        NidOAuthLogin().callProfileApi(object :
                            NidProfileCallback<NidProfileResponse> {
                            override fun onSuccess(result: NidProfileResponse) {
                                name = result.profile?.name.toString()
                                email = result.profile?.email.toString()
                                gender = result.profile?.gender.toString()
                                image = result.profile?.profileImage.toString()
                                phone = result.profile?.mobile.toString()
                                val naverAccessToken = NaverIdLoginSDK.getAccessToken()
                                Log.e(TAG, "로그인 성공")
                                Log.e(TAG, "=================================")
                                Log.e(TAG, "네이버 로그인한 유저 정보 - 이름 : $name")
                                Log.e(TAG, "네이버 로그인한 유저 정보 - 이메일 : $email")
                                Log.e(TAG, "네이버 로그인한 유저 정보 - 성별 : $gender")
                                Log.e(TAG, "네이버 로그인한 유저 정보 - 프로필사진 : $image")
                                Log.e(TAG, "네이버 로그인한 유저 정보 - 전화번호 : $phone")
                                Log.e(TAG, "네이버 로그인한 유저 정보 - 액세스토큰 : $naverAccessToken")

                                // setString Datas
                                prefs.setString("LOGINRESULT", "NAVER")
                                prefs.setString("USEREMAIL", email)
                                prefs.setString("USERIMAGE", image)
                                prefs.setString("ACCESSTOKEN", "$naverAccessToken")

                                // Naver Login List
                                val jwtResponse = MutableLiveData<ResultJWT>()

                                PostTokenAPI.postNaverLoginToken("$naverAccessToken")
                                    .enqueue(object : Callback<ResultJWT> {
                                        override fun onResponse(
                                            call: Call<ResultJWT>,
                                            response: Response<ResultJWT>
                                        ) {
                                            jwtResponse.value = response.body()
                                            Log.d(TAG, jwtResponse.value!!.message)
                                            Log.d(TAG, jwtResponse.value!!.code.toString())

                                            when(jwtResponse.value!!.code) {
                                                // 성공코드
                                                1000 -> {
                                                    prefs.setString("JWT", jwtResponse.value!!.result.jwt)
                                                    prefs.setInt("USERIDX", jwtResponse.value!!.result.userIdx)
                                                    Log.d(TAG, "JWT : ${jwtResponse.value!!.result.jwt}")
                                                    Log.d(TAG, "USERIDX : ${jwtResponse.value!!.result.userIdx}")

                                                    startMain.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                                                    startActivity(startMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))

                                                    showCustomToast("네이버 로그인 성공")
                                                    Log.e(TAG, "네이버 로그인 성공")
                                                }
                                                // 실패코드
                                                else -> {
                                                    startActivity(startNext)
                                                }
                                            }
                                        }

                                        override fun onFailure(
                                            call: Call<ResultJWT>,
                                            t: Throwable
                                        ) {
                                            Log.d(TAG, jwtResponse.value!!.message)
                                            onDestroy()
                                        }
                                    })
                            }

                            override fun onError(errorCode: Int, message: String) {
                                showCustomToast("로그인 에러")
                                Log.e(TAG, "로그인 에러")
                                finish()
                            }

                            override fun onFailure(httpStatus: Int, message: String) {
                                showCustomToast("로그인 실패")
                                Log.e(TAG, "로그인 실패")
                                finish()
                            }
                        })
                    }

                    override fun onError(errorCode: Int, message: String) {
                        val naverAccessToken = NaverIdLoginSDK.getAccessToken()
                        Log.e(TAG, "naverAccessToken : $naverAccessToken")
                        showCustomToast("로그인 실패")
                        Log.e(TAG, "로그인 에러")
                        finish()
                    }

                    override fun onFailure(httpStatus: Int, message: String) {
                        showCustomToast("로그인 실패")
                        Log.e(TAG, "로그인 실패")
                        finish()
                    }
                }

                NaverIdLoginSDK.initialize(
                    this@MiniPageActivity,
                    getString(R.string.naver_client_id),
                    getString(R.string.naver_client_secret),
                    "번개장터_clone"
                )

                NaverIdLoginSDK.authenticate(this@MiniPageActivity, oAuthLoginCallback)
            }
        }

        //Facebook-Login
        callbackManager = CallbackManager.Factory.create() //로그인 응답 처리할 CallbackManager
//        callbackManager = CallbackManagerImpl.RequestCodeOffset.Login.toRequestCode()
        loginManager = LoginManager.getInstance()

        binding.btnFacebookLogin.setOnClickListener {
//            loginManager.logInWithReadPermissions(this, Arrays.asList("public_profile", "email"))
//            loginManager.registerCallback(callbackManager, object : FacebookCallback<LoginResult?> {
//                override fun onSuccess(loginResult: LoginResult?) {
//                    // App code
//                    val graphRequest = GraphRequest.newMeRequest(loginResult?.accessToken) { f_object, response ->
//                        Log.e(TAG, "${loginResult?.accessToken}")
//                        Log.e(TAG, "${f_object}")
////                         {token: loginResult.accessToken / userObject: f_object}
////                        binding.tvFbResult.text = "onSuccess: token: ${loginResult?.accessToken} \n\n userObject: ${f_object}}"
//                    }
//                    val parameters = Bundle()
//                    parameters.putString("fields", "id,name,email,gender,birthday")
//                    graphRequest.parameters = parameters
//                    graphRequest.executeAsync()
//                    Log.e(TAG, "페이스북으로 로그인")
//                    finish()
//                    startActivity(startMain.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
//                }
//
//                override fun onCancel() {
//                    Log.e(TAG, "페이스북 로그인 취소")
//                    finish()
//                }
//                override fun onError(exception: FacebookException) {
////                    binding.tvFbResult.text = "onError: ${exception.printStackTrace()}"
//                    showCustomToast("페이스북 로그인 실패")
//                    Log.e(TAG, "${exception.printStackTrace()}")
//                    Log.e(TAG, "페이스북 로그인 실패")
//                    finish()
//                }
//            })
            showCustomToast("로그인 할 수 없습니다 : v.Beta")
        }

        binding.flBackground.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.none, R.anim.vertical_exit)
        }

        binding.btnMyselfLogin.setOnClickListener {
            finish()
            startActivity(Intent(this, MyPhoneActivity::class.java).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }
    }

        /** 페이스북 로그인에 필요한? 것.. 잘 모르겠음 .. 아마 외부 액티비티 종료시, 데이터를 콜백해주는게 아닌가.. */
        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
            super.onActivityResult(requestCode, resultCode, data)
            val intent = Intent(this, MainActivity::class.java)
            setResult(RESULT_OK, intent)

            try{
                val handled = callbackManager.onActivityResult(requestCode, resultCode, data)
                if (handled) { /* all done */
                    Log.e(TAG, "전송 및 수신 성공")
                    showCustomToast("전송 및 수신 성공")
                } else { /* result wasn't handled by the callback manager, so check for other potential request codes */
                }
            } catch (e:Exception) {
                e.printStackTrace()
                Log.e(TAG, "${requestCode}")
                Log.e(TAG, "${resultCode}")
                Log.e(TAG, "${data}")
                Log.e(TAG, "페이스북 로그인 실패")
                finish()
                startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
            }
        }
}