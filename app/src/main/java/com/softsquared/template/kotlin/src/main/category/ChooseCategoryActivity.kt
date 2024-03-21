package com.softsquared.template.kotlin.src.main.category

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.databinding.ActivityChooseCategoryBinding
import com.softsquared.template.kotlin.src.main.category.big.CategoryBagActivity
import com.softsquared.template.kotlin.src.main.category.big.CategoryManActivity
import com.softsquared.template.kotlin.src.main.category.big.CategoryShoesActivity
import com.softsquared.template.kotlin.src.main.category.big.CategoryWomanActivity
import com.softsquared.template.kotlin.src.main.category.big.middle.WomanPaddingActivity
import com.softsquared.template.kotlin.src.main.goods.PushGoodsActivity
import com.softsquared.template.kotlin.src.main.reqres.GetCategoriesAPI
import com.softsquared.template.kotlin.src.main.reqres.GetUserData
import com.softsquared.template.kotlin.src.main.reqres.RetrofitClient
import com.softsquared.template.kotlin.util.TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ChooseCategoryActivity : BaseActivity<ActivityChooseCategoryBinding>(ActivityChooseCategoryBinding::inflate) {

    /** Api 불러오기 */
    private val GetAllCategoriesAPI = RetrofitClient.apiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        getallCategories()

        prefs.setInt("MAININT", 0)
        prefs.setString("MAINSTR", "null")
        prefs.setInt("MIDDLEINT", 0)
        prefs.setString("MIDDLESTR", "null")
        prefs.setInt("SUBINT", 0)
        prefs.setString("SUBSTR", "null")

        Log.e(TAG, "입력하기 전 ${prefs.getString("MAINSTR", "null")}")

        binding.btnGoBack.setOnClickListener { finish() }

        binding.btnWoman.setOnClickListener {
            prefs.setInt("MAININT", 100)
            prefs.setString("MAINSTR", "여성의류")

            val intent = Intent(this, CategoryWomanActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnMan.setOnClickListener {
            prefs.setInt("MAININT", 200)
            prefs.setString("MAINSTR", "남성의류")

            val intent = Intent(this, CategoryManActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnShoes.setOnClickListener {
            prefs.setInt("MAININT", 300)
            prefs.setString("MAINSTR", "신발")

            val intent = Intent(this, CategoryShoesActivity::class.java)
            startActivity(intent)
            finish()
        }

        binding.btnBag.setOnClickListener {
            prefs.setInt("MAININT", 400)
            prefs.setString("MAINSTR", "가방")

            val intent = Intent(this, CategoryBagActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    /** 모든 카테고리 조회 */
    fun getallCategories() {

        showLoadingDialog(this)

        val requestResult = MutableLiveData<GetCategoriesAPI>()
        val jwt = BaseFragment.prefs.getString("JWT","null")

        GetAllCategoriesAPI.getCategoriesData(jwt)
            .enqueue(object : Callback<GetCategoriesAPI> {
                override fun onResponse(call: Call<GetCategoriesAPI>, response: Response<GetCategoriesAPI>) {
                    requestResult.value = response.body()
                    Log.d(TAG, "응답 메세지 : ${requestResult.value!!.message}")
                    Log.d(TAG, "응답 코드 : ${requestResult.value!!.code}")
                    Log.i(TAG, "카테고리 모든 정보 : ${requestResult.value!!.result}")

                    dismissLoadingDialog()
                }
                override fun onFailure(call: Call<GetCategoriesAPI>, t: Throwable) {
                    Log.d(TAG, "응답 메세지 : ${requestResult.value!!.message}")
                    Log.d(TAG, "응답 코드 : ${requestResult.value!!.code}")

                    dismissLoadingDialog()
                }
            })
    }

//    fun finishActivity() {
//        val intent = Intent(this, PushGoodsActivity::class.java)
//        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//        startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
//    }
}