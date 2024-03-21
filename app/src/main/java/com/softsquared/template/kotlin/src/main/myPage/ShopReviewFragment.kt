package com.softsquared.template.kotlin.src.main.myPage

import android.annotation.SuppressLint
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.databinding.FragmentShopReviewBinding
import com.softsquared.template.kotlin.src.main.home.GoodsData
import com.softsquared.template.kotlin.src.main.home.GoodsRVAdapter
import com.softsquared.template.kotlin.src.main.reqres.GetReviewData
import com.softsquared.template.kotlin.src.main.reqres.RetrofitClient
import com.softsquared.template.kotlin.util.TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ShopReviewFragment : BaseFragment<FragmentShopReviewBinding>(FragmentShopReviewBinding::bind, R.layout.fragment_shop_review) {

    lateinit var reviewRVAdapter: ReviewRVAdapter
    private val datas = mutableListOf<ReviewData>()
    lateinit var recyclerView: RecyclerView

    /** Api 불러오기 */
    private val GetUserDataAPI = RetrofitClient.apiService

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = super.onCreateView(inflater , container, savedInstanceState)

        recyclerView = rootView!!.findViewById(R.id.rv_recommend_goods) as RecyclerView
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecycler()

        /** 해당 상점의 후기 조회하기 */
        var requestReviewData = MutableLiveData<GetReviewData>()
        val getuserdata = prefs.getInt("USERIDX", 0)
        val jwt = prefs.getString("JWT","null")
        Log.e(TAG, "회원 IDX : $getuserdata")

        GetUserDataAPI.getReviewData(jwt, getuserdata)
            .enqueue(object : Callback<GetReviewData> {
                override fun onResponse(call: Call<GetReviewData>, response: Response<GetReviewData>) {
                    requestReviewData.value = response.body()

                    Log.d(TAG, "응답 메세지 : ${requestReviewData.value!!.message}")
                    Log.d(TAG, "응답 코드 : ${requestReviewData.value!!.code}")

                    val count : Int = requestReviewData.value!!.result.size

                    binding.reviewtwo.text = "$count"

                    if (count == 0) {
                        TODO()
                    }

                    try {
                        for (i in 0..count) {
                            addTask(
                                requestReviewData.value!!.result[i].rate,
                                requestReviewData.value!!.result[i].contents,
                                requestReviewData.value!!.result[i].name,
                                requestReviewData.value!!.result[i].createdAt.toString(),
                                requestReviewData.value!!.result[i].title
                            )
                        }
                    } catch (e:Exception) {
                        e.printStackTrace()
                    }

                }
                override fun onFailure(call: Call<GetReviewData>, t: Throwable) {
                    Log.d(TAG, "응답 메세지 : ${requestReviewData.value!!.message}")
                    Log.d(TAG, "응답 코드 : ${requestReviewData.value!!.code}")
                }
            })
    }


    /** 배열 추가 */
    @SuppressLint("NotifyDataSetChanged")
    private fun initRecycler() {
        reviewRVAdapter = ReviewRVAdapter(requireContext())
        binding.rvRecommendGoods.adapter = reviewRVAdapter

        datas.apply {
        }

        reviewRVAdapter.datas = datas
        reviewRVAdapter.notifyDataSetChanged()
    }

    /** 외부에서 배열에 추가할 수 있게 사용하는 곳 */
    @SuppressLint("NotifyDataSetChanged")
    fun addTask(Score: Double, Contents: String, BuyName: String, Date: String, Category: String) {
        //editText의 결과를 가져온다.
        val result = ReviewData(Score, Contents, BuyName, Date, Category)
        datas.add(result)

        binding.rvRecommendGoods.adapter?.notifyDataSetChanged() // 검색결과 배열 갱신
        Log.e(TAG, "onCreateView: $datas")
    }
}