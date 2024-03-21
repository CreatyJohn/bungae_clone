package com.softsquared.template.kotlin.src.main.goods

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import android.text.*
import android.util.Log
import android.view.View
import android.widget.EditText
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.databinding.ActivityPushGoodsBinding
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.category.ChooseCategoryActivity
import com.softsquared.template.kotlin.src.main.home.GoodsRVAdapter
import com.softsquared.template.kotlin.src.main.reqres.*
import com.softsquared.template.kotlin.util.TAG
import okhttp3.internal.notify
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.text.DecimalFormat
import java.util.*
import kotlin.collections.ArrayList

data class ImgData(
    val img : String
)

class PushGoodsActivity : BaseActivity<ActivityPushGoodsBinding>(ActivityPushGoodsBinding::inflate) {

    /** Api 불러오기 */
    private val PostGoodsAPI = RetrofitClient.apiService

    /** 리사이클러뷰 설정 */
    lateinit var imgRVAdapter: ImgRVAdapter
    private val datas = mutableListOf<ImgData>()
    lateinit var recyclerView: RecyclerView

    @SuppressLint("SetTextI18n")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        prefs.setInt("MAININT", 0)

        datas.removeAll(datas)

        initRecycler()
        recyclerView = binding.rvImageRv
        recyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        fun showtext() {
            binding.clWhenEdittext.show()
        }

        fun hidetext() {
            binding.clWhenEdittext.hide()
        }

        binding.llPrice.setOnClickListener { hidetext() }
        binding.etGoodsTitle.setOnClickListener { hidetext() }
        binding.etContents.setOnClickListener { showtext() }
        binding.btnCategoryChoose.setOnClickListener {
            val intent = Intent(this, ChooseCategoryActivity::class.java)
            startActivity(intent)
        }

        val maxLength = 2000
        binding.etContents.filters = arrayOf<InputFilter>(InputFilter.LengthFilter(maxLength))

        /** edittext의 사이즈를 재주는 메서드 사용 (addTextChangedListener) */
        binding.etContents.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}

            @SuppressLint("SetTextI18n")
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                val input: String = binding.etContents.text.toString()
                binding.tvTextCount.text = input.length.toString()
            }

            override fun afterTextChanged(s: Editable) {}
        })

        binding.appbarlayout.bringToFront()

        binding.btnGoBack.setOnClickListener {
            finish()
        }

        /** 상품등록 API */
        binding.btnRegistPay.setOnClickListener {

            showLoadingDialog(this)
            val titleT = binding.etGoodsTitle.text // OK
            Log.d(TAG, "title: $titleT")
            val priceT = binding.etPrice.text
            Log.d(TAG, "price: $priceT")
            val contentsT = binding.etContents.text
            Log.d(TAG, "contents: $contentsT")
            val isSafepayT: Boolean = binding.cbPay.isChecked
            Log.d(TAG, "isSafepay: $isSafepayT")
            val imagelistT = prefs.getStringArray("IMAGERESULT")
            Log.d(TAG, "imagelist: $imagelistT")
            val userIDXT = prefs.getInt("USERIDX", 0) // OK
            Log.d(TAG, "userIDX: $userIDXT")

            val mainCategory = prefs.getInt("MAININT", 0)
            val middleCategory = prefs.getInt("MIDDLEINT", 0)
            val subCategory = prefs.getInt("SUBINT", 0)

//            Log.d(TAG, "$goodsdata")
            val postGoodsData = PostGoodsData(
                "${binding.etGoodsTitle.text}",
                "${binding.etPrice.text}",
                "${binding.etContents.text}",
                binding.cbPay.isChecked,
                imagelistT,
                prefs.getInt("USERIDX", 0),
                mainCategory,
                middleCategory,
                subCategory
            )

            Log.i(TAG, "$postGoodsData")

            val responsePostGoods = MutableLiveData<RequestPostGoods>()

            val mainintent = Intent(this, MainActivity::class.java)
            val jwt = prefs.getString("JWT","null")

            PostGoodsAPI.postGoodsData(jwt, postGoodsData)
                .enqueue(object : Callback<RequestPostGoods> {
                    override fun onResponse(
                        call: Call<RequestPostGoods>,
                        response: Response<RequestPostGoods>
                    ) {
                        responsePostGoods.value = response.body()
                        Log.d(TAG, "응답 메세지 : ${responsePostGoods.value!!.message}")
                        Log.d(TAG, "응답 코드 : ${responsePostGoods.value!!.code}")

                        if (responsePostGoods.value!!.code == 1000) {
                            prefs.setStringArray("IMAGEARRAY", arrayListOf("null"))
                            Log.d(TAG, "상품 IDX : ${responsePostGoods.value!!.result.productIdx}")
                            mainintent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                            startActivity(mainintent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
                            showCustomToast("상품등록 성공")
                            dismissLoadingDialog()
                        } else {
                            showCustomToast("${showCustomToast(responsePostGoods.value!!.message)}")
                            dismissLoadingDialog()
                        }
                    }

                    override fun onFailure(call: Call<RequestPostGoods>, t: Throwable) {
                        Log.d(TAG, "응답 메세지 : ${responsePostGoods.value!!.message}")
                        Log.d(TAG, "응답 코드 : ${responsePostGoods.value!!.code}")
                        showCustomToast("상품등록 실패")
                        dismissLoadingDialog()
                    }
                })
            }

        /** 천단위로 콤마(,) 찍기 메서드 */
        binding.etPrice.addTextChangedListener(CustomTextWatcher(binding.etPrice))

        binding.llSetImage.setOnClickListener{
            val intent = Intent(this, GallaryActivity::class.java)
            startActivity(intent)
        }

    }

    /** 리사이클러뷰 관련 */
    // 배열 추가
    @SuppressLint("NotifyDataSetChanged", "SetTextI18n")
    private fun initRecycler() {
        imgRVAdapter = ImgRVAdapter(this)
        binding.rvImageRv.adapter = imgRVAdapter

        datas.apply {
        }

        imgRVAdapter.datas = datas
        imgRVAdapter.notifyDataSetChanged()

        binding.tvCountImage.text = datas.size.toString()
    }

    // 외부에서 배열에 추가할 수 있게 사용하는 곳
    @SuppressLint("NotifyDataSetChanged")
    fun addTask(Img: String) {
        //editText의 결과를 가져온다.
        val result = ImgData(Img)
        datas.add(result)

        binding.rvImageRv.adapter?.notifyDataSetChanged() // 검색결과 배열 갱신
        Log.e(TAG, "onCreateView: $datas")
    }

    /** 파이어베이스에 로컬 스토리지로 이미지 저장시키기 */
    override fun onRestart() {
        super.onRestart()

        datas.removeAll(datas)
        getimageuri()
    }

    fun getimageuri() {

        showLoadingDialog(this)
        val urimaxsize = prefs.getInt("URIMAXSIZE", 13)
        val imagelistT = prefs.getStringArray("IMAGEARRAY")

        when (urimaxsize) {
            13 -> {
                TODO()
                dismissLoadingDialog()
            }
            0 -> {
                TODO()
                dismissLoadingDialog()
            }
            else -> {
                Log.i(TAG, "PushGoodsActivity 완성된 배열 : $imagelistT")

                for (i in 0 until imagelistT.size) {
                    addTask(imagelistT[i])
                }

                prefs.setStringArray("IMAGERESULT", imagelistT)

                binding.tvCountImage.text = imagelistT.size.toString()
                dismissLoadingDialog()
            }
        }
    }

    override fun onResume() {
        super.onResume()

        val mainCategory = prefs.getInt("MAININT", 0)
        val mainCategoryname = prefs.getString("MAINSTR", "null")
        val middleCategory = prefs.getInt("MIDDLEINT", 0)
        val middleCategoryname = prefs.getString("MIDDLESTR", "null")
        val subCategory = prefs.getInt("SUBINT", 0)
        val subCategoryname = prefs.getString("SUBSTR", "null")

        if (mainCategory == 0) {
            binding.btnCategoryChoose.setTextColor(Color.parseColor("#9e9e9e"))
            binding.btnCategoryChoose.text = "카테고리"
            Log.e(TAG, "카테고리 값 없음")
        } else if (middleCategory == 0){
            binding.btnCategoryChoose.setTextColor(Color.parseColor("#000000"))
            binding.btnCategoryChoose.text = mainCategoryname
            Log.i(TAG, "$mainCategory, $middleCategory, $subCategory")
        } else if (subCategory == 0){
            binding.btnCategoryChoose.setTextColor(Color.parseColor("#000000"))
            binding.btnCategoryChoose.text = "$mainCategoryname > $middleCategoryname"
            Log.i(TAG, "$mainCategory, $middleCategory, $subCategory")
        } else {
            binding.btnCategoryChoose.setTextColor(Color.parseColor("#000000"))
            binding.btnCategoryChoose.text = "$mainCategoryname > $middleCategoryname > $subCategoryname"
            Log.i(TAG, "$mainCategory, $middleCategory, $subCategory")
        }
    }

    override fun onDestroy() {
        super.onDestroy()

        datas.removeAll(datas)
    }
}

/** 천단위로 콤마(,) 찍기 클라스 */
class CustomTextWatcher internal constructor(private val editText: EditText) : TextWatcher {
    var strAmount = ""
    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {}
    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        if (!TextUtils.isEmpty(s.toString()) && s.toString() != strAmount) {
            strAmount = makeStringComma(s.toString().replace(",", ""))
            editText.setText(strAmount)
            val editable = editText.text
            Selection.setSelection(editable, strAmount.length)
        }
    }

    override fun afterTextChanged(s: Editable) {}
    protected fun makeStringComma(str: String): String {    // 천단위 콤마설정.
        if (str.length == 0) {
            return ""
        }
        val value = str.toLong()
        val format = DecimalFormat("###,###")
        return format.format(value)
    }
}