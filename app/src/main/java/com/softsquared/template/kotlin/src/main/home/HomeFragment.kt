package com.softsquared.template.kotlin.src.main.home

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.Menu
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.databinding.FragmentHomeBinding
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.MenuActivity
import com.softsquared.template.kotlin.src.main.NoticeActivity
import com.softsquared.template.kotlin.src.main.goods.ProductDetailActivity
import com.softsquared.template.kotlin.src.main.reqres.GetGoodsData
import com.softsquared.template.kotlin.src.main.reqres.RetrofitClient
import com.softsquared.template.kotlin.src.main.search.CategorySearchActivity
import com.softsquared.template.kotlin.src.main.search.SearchActivity
import com.softsquared.template.kotlin.util.TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class HomeFragment : BaseFragment<FragmentHomeBinding>(FragmentHomeBinding::bind, R.layout.fragment_home){

    private val sliderImageHandler: Handler = Handler()
    private val sliderImageRunnable = Runnable { binding.vpBanner.currentItem += 1 }

    private lateinit var goodsRVAdapter: GoodsRVAdapter
    private val datas = mutableListOf<GoodsData>()
    private lateinit var recyclerView: RecyclerView

    /** Api 불러오기 */
    private val GetAllGoodsAPI = RetrofitClient.apiService

    private lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = super.onCreateView(inflater, container, savedInstanceState)

        recyclerView = rootView!!.findViewById(R.id.rv_goods) as RecyclerView
        recyclerView.layoutManager = GridLayoutManager(requireContext(), 3)

        return rootView
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        showLoadingDialog(requireContext())

        initRecycler()

        /** Actionbar 제거 */
        requireActivity().setStatusBarTransparent()

        /** 광고 배너 주소 */
        val imageList = arrayListOf<Int>().apply {
            for (i in 0..9) {
                add(R.drawable.banner_1)
                add(R.drawable.banner_2)
                add(R.drawable.banner_3)
                add(R.drawable.banner_4)
                add(R.drawable.banner_5)
                add(R.drawable.banner_6)
                add(R.drawable.banner_7)
                add(R.drawable.banner_8)
                add(R.drawable.banner_9)
                add(R.drawable.banner_ten)
            }
        }

        binding.btnList2.setOnClickListener {
            val list = Intent(requireContext(), MenuActivity::class.java)
            startActivity(list)
        }

        binding.btnSearch.setOnClickListener {
            val search = Intent(requireContext(), SearchActivity::class.java)
            startActivity(search)
            mainActivity.overridePendingTransition(R.anim.horizon_enter, R.anim.none)
        }

        binding.vpBanner.apply {
            adapter = BannerPagerAdapter(imageList, binding.vpBanner)
            offscreenPageLimit = 1
            getChildAt(0).overScrollMode = RecyclerView.OVER_SCROLL_NEVER
            registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
                @SuppressLint("UseCompatLoadingForDrawables", "SetTextI18n")
                override fun onPageSelected(position: Int) {
                    super.onPageSelected(position)

                    binding.textViewCurrentBanner.text = "${position % 10 + 1}"

                    sliderImageHandler.removeCallbacks(sliderImageRunnable)
                    sliderImageHandler.postDelayed(sliderImageRunnable, 4000)
                }
            })
        }

        binding.mainScrollView.run {
            try {
                header = binding.appbarlayout
                stickListener = { _ ->
                    Log.e(TAG, "stickListener")
                    binding.appbarlayout.setBackgroundResource(R.color.white)
                    binding.btnList.setImageResource(R.drawable.ic_list_black)
                    binding.btnNoticeBell.setImageResource(R.drawable.ic_notification_black)
                    binding.btnSearch.setImageResource(R.drawable.ic_search_black)
                }
                freeListener = { _ ->
                    Log.e(TAG, "freeListener")
                    binding.appbarlayout.setBackgroundResource(R.color.transparent)
                    binding.btnList.setImageResource(R.drawable.ic_list)
                    binding.btnNoticeBell.setImageResource(R.drawable.ic_notification)
                    binding.btnSearch.setImageResource(R.drawable.ic_search)
                }
            } catch (e: Exception){
                Log.e(TAG, "에러남 ${e.printStackTrace()}")
            }
        }

        binding.btnMans.setOnClickListener {
            val str = binding.tvMan.text.toString()
            BaseActivity.prefs.setString("CATEGORYSTR", str)
            BaseActivity.prefs.setInt("CATEGORYINT", 200)
            val intent = Intent(requireContext(), CategorySearchActivity::class.java)
            startActivity(intent)
        }

        binding.btnMans2.setOnClickListener {
            val str = binding.tvMans2.text.toString()
            BaseActivity.prefs.setString("CATEGORYSTR", str)
            BaseActivity.prefs.setInt("CATEGORYINT", 200)
            val intent = Intent(requireContext(), CategorySearchActivity::class.java)
            startActivity(intent)
        }

        binding.btnList.setOnClickListener {
            val intent = Intent(requireContext(), MenuActivity::class.java)
            startActivity(intent)
        }

        goodsRVAdapter.setOnItemClickListener(object : GoodsRVAdapter.OnItemClickListener{
            override fun onItemClick(v: View, data: GoodsData, pos: Int) {
                Intent(requireContext(), ProductDetailActivity::class.java).apply {
                    prefs.setInt("PRODUCTIDX", data.productidx)
                    addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                }.run { startActivity(this) }
            }
        })

        binding.btnNoticeBell.setOnClickListener {
            val intent = Intent(requireContext(), NoticeActivity::class.java)
            startActivity(intent)
        }

        /** 모든 상품 조회하기 */
        val getallGoods = MutableLiveData<GetGoodsData>()
        val jwt = prefs.getString("JWT","null")

        GetAllGoodsAPI.getAllGoods(jwt).enqueue(object : Callback<GetGoodsData> {
                override fun onResponse(call: Call<GetGoodsData>, response: Response<GetGoodsData>) {
                    getallGoods.value = response.body()

                    val goodscount : Int = getallGoods.value!!.result.size
                    if (goodscount == 0) {
                        TODO()
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

                    } catch (e:Exception) {
                        Log.e(TAG, "onResponseERROR: ${e.printStackTrace()}", )
                    }
                }
                override fun onFailure(call: Call<GetGoodsData>, t: Throwable) {
                    Log.d(TAG, "응답 메세지 : ${getallGoods.value!!.message}")
                    Log.d(TAG, "응답 코드 : ${getallGoods.value!!.code}")
                }
            })

        handlerLoadingDialog()
    }

    /** 배열 추가 */
    @SuppressLint("NotifyDataSetChanged")
    private fun initRecycler() {
        goodsRVAdapter = GoodsRVAdapter(requireContext())
        binding.rvGoods.adapter = goodsRVAdapter

        datas.apply {
        }

        goodsRVAdapter.datas = datas
        goodsRVAdapter.notifyDataSetChanged()
    }

    /** 외부에서 배열에 추가할 수 있게 사용하는 곳 */
    @SuppressLint("NotifyDataSetChanged")
    fun addTask(ProductsIDX: Int, Price: String, Info: String, Image: String, IsFav: Boolean, SafePay: Boolean) {
        //editText의 결과를 가져온다.
        val result = GoodsData(ProductsIDX, Price, Info, Image, IsFav, SafePay)
        datas.add(result)

        binding.rvGoods.adapter?.notifyDataSetChanged() // 검색결과 배열 갱신
        Log.e(TAG, "onCreateView: $datas")
    }

    /** 상태바 한계 없애기 */
    private fun Activity.setStatusBarTransparent() {
        window.apply {
            setFlags(
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS,
                WindowManager.LayoutParams.FLAG_LAYOUT_NO_LIMITS
            )
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
