package com.softsquared.template.kotlin.src.main.myPage

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.MutableLiveData
import androidx.viewpager.widget.ViewPager.OnPageChangeListener
import com.google.android.material.tabs.TabLayout.*
import com.google.android.material.tabs.TabLayoutMediator
import com.softsquared.template.kotlin.R
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.databinding.FragmentProfileBinding
import com.softsquared.template.kotlin.src.main.MainActivity
import com.softsquared.template.kotlin.src.main.NoticeActivity
import com.softsquared.template.kotlin.src.main.reqres.GetCategoriesNumAPI
import com.softsquared.template.kotlin.src.main.reqres.GetReviewData
import com.softsquared.template.kotlin.src.main.reqres.GetUserData
import com.softsquared.template.kotlin.src.main.reqres.RetrofitClient
import com.softsquared.template.kotlin.util.TAG
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ProfileFragment :
    BaseFragment<FragmentProfileBinding>(FragmentProfileBinding::bind, R.layout.fragment_profile) {

    lateinit var mainActivity: MainActivity

    override fun onAttach(context: Context) {
        super.onAttach(context)

        mainActivity = context as MainActivity
    }

    /** Api 불러오기 */
    private val GetUserDataAPI = RetrofitClient.apiService

    @SuppressLint("CheckResult")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        /** userIdx로 특정 회원의 정보 조회하기 : 상점이름 가져오기 */
        val getuserdata = prefs.getInt("USERIDX", 0)
        Log.e(TAG, "회원 IDX : $getuserdata")

        val loginresult = prefs.getString("LOGINRESULT", "null")
        Log.e(TAG, loginresult)

        val getstorename = prefs.getString("STORENAME", "null")
        Log.e(TAG, getstorename)

        /** 사용자 프로필 편집으로 이동 */
        binding.tvEditProfile.setOnClickListener {
            val intent = Intent(requireContext(), EditUserDataActivity::class.java)
            startActivity(intent)
        }

        /** 탭 레이아웃 + 뷰페이저 https://onlyfor-me-blog.tistory.com/295 */
        val adapter = RVPageAdapterTab1(mainActivity.supportFragmentManager)

        adapter.addFragment(SaleProductFragment(), "판매상품")
        adapter.addFragment(ShopReviewFragment(), "상점후기")
        adapter.addFragment(Fragment(), "찜하기")

        binding.afterLoginViewpager.adapter = adapter
        binding.afterLoginTablayout.setupWithViewPager(binding.afterLoginViewpager)

        /** 탭별로 특이사항 실행 */
        binding.afterLoginTablayout.addOnTabSelectedListener(object : OnTabSelectedListener {
            override fun onTabSelected(tab: Tab) { // 선택 X -> 선택 O
                when (tab.position) {
                    0 -> { //탭레이아웃 포지션 얻기 0 이 Tab 1
                        Log.e(TAG, "판매상품 탭")
                    }
                    1 -> {
                        Log.e(TAG, "상점후기 탭")
                    }
                    2 -> {
                        Log.e(TAG, "찜하기 탭, 액티비티 실행")
                        startActivity(Intent(requireContext(), WishListActivity::class.java))
                    }
                }
            }
            override fun onTabUnselected(tab: Tab) {}
            override fun onTabReselected(tab: Tab) {
                Log.e(TAG, "onTabReselected")
            }
        })

        /** 페이지가 넘어갈때마다 있는 감시 리스너 */
        binding.afterLoginViewpager.addOnPageChangeListener(object : OnPageChangeListener {
            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {
                when (position) {
                    0 -> { //탭레이아웃 포지션 얻기 0 이 Tab 1
                        Log.e(TAG, "판매상품 탭")
                    }
                    1 -> {
                        Log.e(TAG, "상점후기 탭")
                    }
                    2 -> {
                        Log.e(TAG, "찜하기 탭, 액티비티 실행")
                    }
                }
            }
            override fun onPageSelected(position: Int) {
            }
            override fun onPageScrollStateChanged(state: Int) {
            }
        })

        /** 탭 레이아웃이 스크롤에 딸려가는 함수 */
        binding.mainScrollView.run {
            try {
                header = binding.clForTablayout
                stickListener = { _ ->
                    Log.e(TAG, "stickListener")
                }
                freeListener = { _ ->
                    Log.e(TAG, "freeListener")
                }
            } catch (e: Exception){
                Log.e(TAG, "에러남 : ${e.printStackTrace()}")
            }
        }

        binding.btnNoticeProfile.setOnClickListener {
            val intent = Intent(requireContext(), NoticeActivity::class.java)
            startActivity(intent)
        }

        /** 메뉴를 눌렀을 시, 바텀시트가 나오는 동작 */
        binding.btnMenuProfile.setOnClickListener {
            val bottomSheet = BottomSheetDialogSetting()
            bottomSheet.show(mainActivity.supportFragmentManager, bottomSheet.tag)
        }

        binding.appbarlayout.bringToFront()
    }

    override fun onStart() {
        super.onStart()

        showLoadingDialog(requireContext())

        var requestPostGoods = MutableLiveData<GetUserData>()
        val getuserdata = prefs.getInt("USERIDX", 0)
        val jwt = prefs.getString("JWT","null")
        Log.e(TAG, "회원 IDX : $getuserdata")

        GetUserDataAPI.getUserData(jwt, getuserdata)
            .enqueue(object : Callback<GetUserData> {
                override fun onResponse(call: Call<GetUserData>, response: Response<GetUserData>) {
                    requestPostGoods.value = response.body()
                    Log.d(TAG, "응답 메세지 : ${requestPostGoods.value!!.message}")
                    Log.d(TAG, "응답 코드 : ${requestPostGoods.value!!.code}")
                    Log.d(TAG, "상점 이름 : ${requestPostGoods.value!!.result.name}")

                    binding.tvMyId.text = requestPostGoods.value!!.result.name

                    Log.d(TAG, "상점 소개글 : ${requestPostGoods.value!!.result.introduce}")

                    binding.tvAverage.text = "${requestPostGoods.value!!.result.rate}"
                    binding.tvBought.text = "${requestPostGoods.value!!.result.sellCnt}"
                    binding.tvFollower.text = "${requestPostGoods.value!!.result.followerCnt}"
                    binding.tvFollowing.text = "${requestPostGoods.value!!.result.followingCnt}"
                    binding.tvRegistDate.text = requestPostGoods.value!!.result.createdAt
                    binding.tvDoAuth.text = "${requestPostGoods.value!!.result.auth}"

                    try {
                        prefs.setString("USERINTRODUCE", requestPostGoods.value!!.result.introduce)
                    } catch (e:Exception) {
                        e.printStackTrace()
                    }
                    dismissLoadingDialog()
                }
                override fun onFailure(call: Call<GetUserData>, t: Throwable) {
                    Log.d(TAG, "응답 메세지 : ${requestPostGoods.value!!.message}")
                    Log.d(TAG, "응답 코드 : ${requestPostGoods.value!!.code}")
                    dismissLoadingDialog()
                }
            })
    }
}