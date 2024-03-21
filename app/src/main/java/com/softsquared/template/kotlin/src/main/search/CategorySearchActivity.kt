package com.softsquared.template.kotlin.src.main.search

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.databinding.ActivityCategorySearchBinding
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.goods.ProductDetailActivity
import com.softsquared.template.kotlin.src.main.home.GoodsData
import com.softsquared.template.kotlin.src.main.home.GoodsRVAdapter
import com.softsquared.template.kotlin.src.main.home.HomeFragment
import com.softsquared.template.kotlin.src.main.home.SearchGoodsRVAdapter
import com.softsquared.template.kotlin.src.main.reqres.GetCategoriesNumAPI
import com.softsquared.template.kotlin.src.main.reqres.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class CategorySearchActivity : BaseActivity<ActivityCategorySearchBinding>(ActivityCategorySearchBinding::inflate) {

    /** 리사이클러뷰 설정 */
    lateinit var searchgoodsRVAdapter: SearchGoodsRVAdapter
    private val datas = mutableListOf<GoodsData>()
    lateinit var recyclerView: RecyclerView

    /** Api 불러오기 */
    private val GetCategoriesAPI = RetrofitClient.apiService

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.llAppbar.bringToFront()

        datas.removeAll(datas)
        recyclerView = binding.rvCategory
        recyclerView.layoutManager = GridLayoutManager(this, 3)

        val string = prefs.getString("CATEGORYSTR", "null")
        val number = prefs.getInt("CATEGORYINT", 0)

        initRecycler()
        binding.tvCategoryName.text = string
        binding.btnGoBack.setOnClickListener { finish() }
        binding.btnSearch.setOnClickListener {
            finish()
            startActivity(Intent(this, SearchActivity::class.java))
        }
        binding.btnGoHome.setOnClickListener {
            finish()
            startActivity(Intent(this, MainActivity::class.java))
        }

        val intent = Intent(this, ProductDetailActivity::class.java)

        searchgoodsRVAdapter.setOnItemClickListener(object : SearchGoodsRVAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: GoodsData, pos: Int) {
                intent.apply {
                    BaseFragment.prefs.setInt("PRODUCTIDX", data.productidx)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { startActivity(this) }
            }
        })

        val requestResult = MutableLiveData<GetCategoriesNumAPI>()
        val jwt = BaseFragment.prefs.getString("JWT","null")

        showLoadingDialog(this)
        GetCategoriesAPI.getCategoriesDataNum(jwt, number)
            .enqueue(object : Callback<GetCategoriesNumAPI> {
                override fun onResponse(call: Call<GetCategoriesNumAPI>, response: Response<GetCategoriesNumAPI>) {
                    requestResult.value = response.body()
                    Log.d(com.softsquared.template.kotlin.util.TAG, "응답 메세지 : ${requestResult.value!!.message}")
                    Log.d(com.softsquared.template.kotlin.util.TAG, "응답 코드 : ${requestResult.value!!.code}")
                    Log.i(com.softsquared.template.kotlin.util.TAG, "카테고리 모든 정보 : ${requestResult.value!!.result}")

                    val goodscount : Int = requestResult.value!!.result.size

                    if (goodscount == 0) {
                        binding.rvCategory.hide()
                        binding.ivGoodsNull.show()
                        showCustomToast("해당 키워드의 상품이 없습니다")
                        dismissLoadingDialog()
                    } else {
                        binding.rvCategory.show()
                        binding.ivGoodsNull.hide()
                    }

                    try {
                        for (i in 0..goodscount) {
                            addTask(
                                requestResult.value!!.result[i].productIdx,
                                requestResult.value!!.result[i].price,
                                requestResult.value!!.result[i].title,
                                requestResult.value!!.result[i].imageUrl,
                                requestResult.value!!.result[i].isFav,
                                requestResult.value!!.result[i].isSafepay)
                        }
                        dismissLoadingDialog()
                    } catch (e:Exception) {
                        Log.e(com.softsquared.template.kotlin.util.TAG, "onResponseERROR: ${e.printStackTrace()}", )
                        dismissLoadingDialog()
                    }

                    dismissLoadingDialog()
                }
                override fun onFailure(call: Call<GetCategoriesNumAPI>, t: Throwable) {
                    Log.d(com.softsquared.template.kotlin.util.TAG, "응답 메세지 : ${requestResult.value!!.message}")
                    Log.d(com.softsquared.template.kotlin.util.TAG, "응답 코드 : ${requestResult.value!!.code}")

                    dismissLoadingDialog()
                }
            })
    }

    /** 리사이클러뷰 관련 */
    // 배열 추가
    @SuppressLint("NotifyDataSetChanged")
    private fun initRecycler() {
        searchgoodsRVAdapter = SearchGoodsRVAdapter(this)
        binding.rvCategory.adapter = searchgoodsRVAdapter

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

        binding.rvCategory.adapter?.notifyDataSetChanged() // 검색결과 배열 갱신
        Log.e(com.softsquared.template.kotlin.util.TAG, "onCreateView: ${datas}")
    }
}