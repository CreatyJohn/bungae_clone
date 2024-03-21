package com.softsquared.template.kotlin.src.main.myPage

import android.annotation.SuppressLint
import android.graphics.Color
import android.os.Bundle
import android.text.Editable
import android.text.InputFilter
import android.text.TextWatcher
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityEditUserDataBinding
import com.softsquared.template.kotlin.src.main.reqres.Introduce
import com.softsquared.template.kotlin.src.main.reqres.JustResponse
import com.softsquared.template.kotlin.src.main.reqres.MyName
import com.softsquared.template.kotlin.src.main.reqres.RetrofitClient
import com.softsquared.template.kotlin.util.TAG
import retrofit2.Call
import retrofit2.Response

class EditUserDataActivity : BaseActivity<ActivityEditUserDataBinding>(ActivityEditUserDataBinding::inflate){

    /** Api 불러오기 */
    private val UserDataAPI = RetrofitClient.apiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val username = prefs.getString("USERNAME", "null")
        binding.etStoreId.setText(username)

        val userinfo = prefs.getString("USERINTRODUCE", "")
        binding.etStoreContents.setText(userinfo)

        val maxLength = 1000
        binding.etStoreContents.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))

        /** edittext의 사이즈를 재주는 메서드 사용 (addTextChangedListener) */
        binding.etStoreContents.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
            @SuppressLint("SetTextI18n")
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val input: String = binding.etStoreContents.text.toString()
                binding.tvTextCount.text = input.length.toString()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        /** edittext에 입력되는 값의 유무에 따라 버튼 활성화 */
        binding.etStoreId.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            /** 값 변경 시 실행되는 함수 */
            @SuppressLint("SetTextI18n")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if(binding.etStoreId.length() > 0) {
                    /** 값 유무에 따른 활성화 여부 */
                    binding.btnApply.isEnabled = true// editText에 값이 있다면 true 없다면 false

                    if (binding.btnApply.isEnabled) {
                        binding.btnApply.setBackgroundResource(R.drawable.rectangle_ripple_effect)

                    } else {
                        binding.btnApply.setBackgroundResource(R.drawable.rectangle_ripple_effect2)
                    }
                } else {
                    /** 값 유무에 따른 활성화 여부 */
                    binding.btnApply.isEnabled = false// editText에 값이 있다면 true 없다면 false

                    if (binding.btnApply.isEnabled) {
                        binding.btnApply.setBackgroundResource(R.drawable.rectangle_ripple_effect)

                    } else {
                        binding.btnApply.setBackgroundResource(R.drawable.rectangle_ripple_effect2)
                    }
                }

                if (binding.etStoreId.length() in 0..2)
                {
                    binding.appCompatTextView.text = "상점명은 최소 2자, 최대 10자까지 입력 가능합니다."
                    binding.appCompatTextView.setTextColor(Color.parseColor("#D21404"))
                    binding.view2.setBackgroundColor(Color.parseColor("#D21404"))
                } else {
                    binding.appCompatTextView.text = "한글, 영어, 숫자만 사용할 수 있어요. (최대 10자)"
                    binding.appCompatTextView.setTextColor(Color.parseColor("#000000"))
                    binding.view2.setBackgroundColor(Color.parseColor("#000000"))
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })

        binding.btnApply.setOnClickListener {
            val resultPatchUser = MutableLiveData<JustResponse>()
            val myname = MyName(binding.etStoreId.text.toString())
            val introduce = Introduce(binding.etStoreContents.text.toString())
            val userIdx = prefs.getInt("USERIDX", 0)
            val jwt = prefs.getString("JWT","null")

            /** 내 상점 이름 수정 */
            UserDataAPI.patchUserData(jwt, myname, userIdx)
                .enqueue(object : retrofit2.Callback<JustResponse> {
                    override fun onResponse(call: Call<JustResponse>, response: Response<JustResponse>) {
                        resultPatchUser.value = response.body()

                        Log.d(TAG, "요청코드 : ${resultPatchUser.value!!.code}")
                        Log.d(TAG, "요청코드 : ${resultPatchUser.value!!.message}")

                        prefs.setString("USERNAME", binding.etStoreId.text.toString())
                        Log.e(TAG, "상점이름 : ${binding.etStoreId.text.toString()}")

                        finish()
                    }
                    override fun onFailure(call: Call<JustResponse>, t: Throwable) {
                        t.printStackTrace()

                        Log.d(TAG, "요청코드 : ${resultPatchUser.value!!.code}")
                        Log.d(TAG, "요청코드 : ${resultPatchUser.value!!.message}")

                        finish()
                    }
                })

            /** 내 상점 정보 수정 */
            UserDataAPI.patchStoreInfoData(jwt, introduce, userIdx)
                .enqueue(object : retrofit2.Callback<JustResponse> {
                    override fun onResponse(call: Call<JustResponse>, response: Response<JustResponse>) {
                        resultPatchUser.value = response.body()

                        Log.d(TAG, "요청코드 : ${resultPatchUser.value!!.code}")
                        Log.d(TAG, "요청코드 : ${resultPatchUser.value!!.message}")

                        prefs.setString("USERINTRODUCE", binding.etStoreContents.text.toString())
                        Log.e(TAG, "상점정보 : ${binding.etStoreContents.text.toString()}")

                        finish()
                    }
                    override fun onFailure(call: Call<JustResponse>, t: Throwable) {
                        t.printStackTrace()

                        Log.d(TAG, "요청코드 : ${resultPatchUser.value!!.code}")
                        Log.d(TAG, "요청코드 : ${resultPatchUser.value!!.message}")

                        finish()
                    }
                })
        }

        binding.btnGoBack.setOnClickListener { finish() }
    }
}