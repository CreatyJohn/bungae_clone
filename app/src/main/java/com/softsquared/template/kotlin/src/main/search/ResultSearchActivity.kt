package com.softsquared.template.kotlin.src.main.search

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.databinding.ActivityResultSearchBinding
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.goods.ProductDetailActivity
import com.softsquared.template.kotlin.src.main.home.GoodsData
import com.softsquared.template.kotlin.src.main.home.GoodsRVAdapter
import com.softsquared.template.kotlin.src.main.home.SearchGoodsRVAdapter
import com.softsquared.template.kotlin.src.main.reqres.GetGoodsData
import com.softsquared.template.kotlin.src.main.reqres.RetrofitClient
import com.softsquared.template.kotlin.util.TAG
import okhttp3.internal.notify
import okhttp3.internal.notifyAll
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.math.log

class ResultSearchActivity : BaseActivity<ActivityResultSearchBinding>(ActivityResultSearchBinding::inflate) {

    /** 리사이클러뷰 설정 */
    lateinit var searchgoodsRVAdapter: SearchGoodsRVAdapter
    private val datas = mutableListOf<GoodsData>()
    lateinit var recyclerView: RecyclerView

    /** Api 불러오기 */
    private val GetAllGoodsAPI = RetrofitClient.apiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        datas.removeAll(datas)
        recyclerView = binding.rvGoodsResult
        recyclerView.layoutManager = GridLayoutManager(this, 3)

        binding.appbarlayout.bringToFront()

        binding.ivGoodsNull.hide()
        binding.rvGoodsResult.show()

        resultKeywordsAPI.invoke()
        pressRecommends.invoke()
        initRecycler()
        checkEditText.invoke()

        binding.btnGoBack.setOnClickListener { finish() }
        binding.btnGoHome.setOnClickListener {
            val intent = Intent(this, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP))
        }
        binding.btnCheckText.setOnClickListener {
            binding.etSearch.setText("")
        }

        /** 키보드의 검색 버튼을 눌렀을 시, 실행되는 메서드 */
        binding.etSearch.setOnEditorActionListener(TextView.OnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                resultAPI.invoke()
                return@OnEditorActionListener true
            }
            false
        })

        searchgoodsRVAdapter.setOnItemClickListener(object : SearchGoodsRVAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: GoodsData, pos: Int) {
                Intent(this@ResultSearchActivity, ProductDetailActivity::class.java).apply {
                    putExtra("PRODUCTIDX", data.productidx)
                    BaseFragment.prefs.setInt("PRODUCTIDX", data.productidx)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { startActivity(this) }
            }
        })
    }

    /** 수동적으로 상품 검색 */
    val resultAPI : () -> Unit = {

        datas.removeAll(datas)

        val setText = binding.etSearch.text.toString()

        val getallGoods = MutableLiveData<GetGoodsData>()
        val jwt = prefs.getString("JWT","null")

        showLoadingDialog(this)
        GetAllGoodsAPI.getGoods(jwt, setText).enqueue(object : Callback<GetGoodsData> {
            override fun onResponse(call: Call<GetGoodsData>, response: Response<GetGoodsData>) {
                getallGoods.value = response.body()

                val goodscount : Int = getallGoods.value!!.result.size

                if (goodscount == 0) {
                    binding.rvGoodsResult.hide()
                    binding.ivGoodsNull.show()
                    showCustomToast("해당 키워드의 상품이 없습니다")
                    dismissLoadingDialog()
                } else {
                    binding.rvGoodsResult.show()
                    binding.ivGoodsNull.hide()
                }

                Log.d(TAG, "응답 메세지 : ${getallGoods.value!!.message}")
                Log.d(TAG, "응답 코드 : ${getallGoods.value!!.code}")
                Log.d(TAG, "상품 갯수 : $goodscount")

                try {
                    for (i in 0..goodscount) {
                        addTask(
                            getallGoods.value!!.result[i].productIdx,
                            getallGoods.value!!.result[i].price,
                            getallGoods.value!!.result[i].title,
                            getallGoods.value!!.result[i].imageUrl,
                            getallGoods.value!!.result[i].isFav,
                            getallGoods.value!!.result[i].isSafepay)
                        Log.i(TAG, "찜하기 : ${getallGoods.value!!.result[i].isFav}")
                        Log.i(TAG, "번개페이 : ${getallGoods.value!!.result[i].isSafepay}")
                    }
                    dismissLoadingDialog()
                } catch (e:Exception) {
                    Log.e(TAG, "onResponseERROR: ${e.printStackTrace()}", )
                    dismissLoadingDialog()
                }
            }
            override fun onFailure(call: Call<GetGoodsData>, t: Throwable) {
                Log.d(TAG, "응답 메세지 : ${getallGoods.value!!.message}")
                Log.d(TAG, "응답 코드 : ${getallGoods.value!!.code}")
                dismissLoadingDialog()
            }
        })
    }

    /** 키워드클릭시 상품 이름 자동 입력과 검색 */
    val resultKeywordsAPI : () -> Unit = {
        /** 키워드로 상품 조회하기 */
        datas.removeAll(datas)

        val setText = prefs.getString("SEARCH", "null")
        binding.etSearch.setText(setText)

        val getallGoods = MutableLiveData<GetGoodsData>()
        val jwt = prefs.getString("JWT","null")

        showLoadingDialog(this)
        GetAllGoodsAPI.getGoods(jwt, setText).enqueue(object : Callback<GetGoodsData> {
            override fun onResponse(call: Call<GetGoodsData>, response: Response<GetGoodsData>) {
                getallGoods.value = response.body()

                val goodscount : Int = getallGoods.value!!.result.size

                if (goodscount == 0) {
                    binding.rvGoodsResult.hide()
                    binding.ivGoodsNull.show()
                    showCustomToast("해당 키워드의 상품이 없습니다")
                    Log.e(TAG, "오류잡기 0")
                    dismissLoadingDialog()
                } else {
                    binding.rvGoodsResult.show()
                    binding.ivGoodsNull.hide()
                }

                Log.d(TAG, "응답 메세지 : ${getallGoods.value!!.message}")
                Log.d(TAG, "응답 코드 : ${getallGoods.value!!.code}")
                Log.d(TAG, "상품 갯수 : $goodscount")

                try {
                    for (i in 0..goodscount) {
                        addTask(
                            getallGoods.value!!.result[i].productIdx,
                            getallGoods.value!!.result[i].price,
                            getallGoods.value!!.result[i].title,
                            getallGoods.value!!.result[i].imageUrl,
                            getallGoods.value!!.result[i].isFav,
                            getallGoods.value!!.result[i].isSafepay)
                    }
                    Log.e(TAG, "오류잡기 1")
                    dismissLoadingDialog()
                } catch (e:Exception) {
                    Log.e(TAG, "onResponseERROR: ${e.printStackTrace()}", )
                    Log.e(TAG, "오류잡기 2")
                    dismissLoadingDialog()
                }
            }
            override fun onFailure(call: Call<GetGoodsData>, t: Throwable) {
                Log.d(TAG, "응답 메세지 : ${getallGoods.value!!.message}")
                Log.d(TAG, "응답 코드 : ${getallGoods.value!!.code}")
                Log.e(TAG, "오류잡기 3")
                dismissLoadingDialog()
            }
        })
    }

    /** 리사이클러뷰 관련 */
    // 배열 추가
    @SuppressLint("NotifyDataSetChanged")
    private fun initRecycler() {
        searchgoodsRVAdapter = SearchGoodsRVAdapter(this)
        binding.rvGoodsResult.adapter = searchgoodsRVAdapter

        datas.apply {
        }

        searchgoodsRVAdapter.datas = datas
        searchgoodsRVAdapter.notifyDataSetChanged()
    }

    // 외부에서 배열에 추가할 수 있게 사용하는 곳
    @SuppressLint("NotifyDataSetChanged")
    fun addTask(ProductsIDX: Int, Price: String, Info: String, Image: String, IsFav: Boolean, SafePay: Boolean) {
        //editText의 결과를 가져온다.
        val result = GoodsData(ProductsIDX, Price, Info, Image, IsFav, SafePay)
        datas.add(result)

        binding.rvGoodsResult.adapter?.notifyDataSetChanged() // 검색결과 배열 갱신
        Log.e(TAG, "onCreateView: ${datas}")
    }

    /** edittext의 유무를 체크하는 람다 함수 */
    val checkEditText : () -> Unit = {
        /** edittext에 입력되는 값의 유무에 따라 버튼 활성화 */
        binding.etSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            /** 값 변경 시 실행되는 함수 */
            @SuppressLint("SetTextI18n")
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {

                if(binding.etSearch.length() > 0) {
                    /** 값 유무에 따른 활성화 여부 */
                    binding.btnCheckText.isEnabled = true// editText에 값이 있다면 true 없다면 false

                    if (binding.btnCheckText.isEnabled) {
                        binding.btnCheckText.show()

                    } else {
                        binding.btnCheckText.hide()
                    }
                } else {
                    /** 값 유무에 따른 활성화 여부 */
                    binding.btnCheckText.isEnabled = false// editText에 값이 있다면 true 없다면 false

                    if (binding.btnCheckText.isEnabled) {
                        binding.btnCheckText.show()

                    } else {
                        binding.btnCheckText.hide()
                    }
                }
            }
            override fun afterTextChanged(s: Editable?) {
            }
        })
    }

    /** 해당 추천어를 누르면 누른 추천어로 검색 됨 */
    val pressRecommends : () -> Unit = {
        binding.llFirstRecommends.setOnClickListener {
            val textvalue = binding.tvFirstRecommend.text
            binding.etSearch.setText(textvalue)
            performSearch()
        }

        binding.llSecondRecommends.setOnClickListener {
            val textvalue = binding.tvSecondRecommend.text
            binding.etSearch.setText(textvalue)
            performSearch()
        }

        binding.llThirdRecommends.setOnClickListener {
            val textvalue = binding.tvThirdRecommend.text
            binding.etSearch.setText(textvalue)
            performSearch()
        }

        binding.llFourthRecommends.setOnClickListener {
            val textvalue = binding.tvFourthRecommend.text
            binding.etSearch.setText(textvalue)
            performSearch()
        }

        binding.llFifthRecommends.setOnClickListener {
            val textvalue = binding.tvFifthRecommend.text
            binding.etSearch.setText(textvalue)
            performSearch()
        }

        binding.llSixthRecommends.setOnClickListener {
            val textvalue = binding.tvSixthRecommend.text
            binding.etSearch.setText(textvalue)
            performSearch()
        }

        binding.llSeventhRecommends.setOnClickListener {
            val textvalue = binding.tvSeventhRecommend.text
            binding.etSearch.setText(textvalue)
            performSearch()
        }

        binding.llEighthRecommends.setOnClickListener {
            val textvalue = binding.tvEighthRecommend.text
            binding.etSearch.setText(textvalue)
            performSearch()
        }

        binding.llNinethRecommends.setOnClickListener {
            val textvalue = binding.tvNinethRecommend.text
            binding.etSearch.setText(textvalue)
            performSearch()
        }

        binding.llTenthRecommends.setOnClickListener {
            val textvalue = binding.tvTenthRecommend.text
            binding.etSearch.setText(textvalue)
            performSearch()
        }
    }
    private fun performSearch() {
        prefs.setString("SEARCH", binding.etSearch.text.toString())
        datas.removeAll(datas)
        resultKeywordsAPI.invoke()
    }
}