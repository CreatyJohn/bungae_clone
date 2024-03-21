package com.softsquared.template.kotlin.src.main.goods

import android.annotation.SuppressLint
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.softsquared.template.kotlin.config.BaseActivity
import com.softsquared.template.kotlin.config.BaseFragment
import com.softsquared.template.kotlin.databinding.ActivityProductDetailBinding
import com.softsquared.template.kotlin.src.main.reqres.*
import com.softsquared.template.kotlin.src.main.search.ResultSearchActivity
import com.softsquared.template.kotlin.util.TAG
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.IOException
import java.net.MalformedURLException
import java.net.URL
import kotlin.math.log

class ProductDetailActivity : BaseActivity<ActivityProductDetailBinding>(ActivityProductDetailBinding::inflate) {

    object imageLoader {
        fun loadImage(imageUrl: String): Bitmap? {
            val bmp: Bitmap? = null
            try {
                val url = URL(imageUrl)
                val stream = url.openStream()

                return BitmapFactory.decodeStream(stream)
            } catch (e: MalformedURLException) {
                e.printStackTrace()
            } catch (e: IOException) {
                e.printStackTrace()
            }
            return bmp
        }
    }

    /** Api 불러오기 */
    private val GetProductIdx = RetrofitClient.apiService
    val jwt = BaseFragment.prefs.getString("JWT","null")
    val data = prefs.getInt("PRODUCTIDX", -1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding.btnGoBack.setOnClickListener { finish() }
        binding.btnSearch.setOnClickListener {
            val intent = Intent(this, ResultSearchActivity::class.java)
            prefs.setString("SEARCH", "")
            startActivity(intent)
        }

        /** 특정상품조회 (이미지제외), 특정상품이미지 조회 */
        val requestGetGoods = MutableLiveData<GetSpecialGoodsData>()
        val requestGetGoodsImage = MutableLiveData<GetGoodsImageAPI>()
        val requestLikeGoods = MutableLiveData<PostFavorite>()

        Log.i(TAG, "상품번호 : $data")
        Log.i(TAG, "회원 JWT : $jwt")

        binding.btnLike.setOnClickListener {
            showLoadingDialog(this)
            GetProductIdx.postProductFav(jwt, data)
                .enqueue(object : Callback<PostFavorite> {
                    override fun onResponse(call: Call<PostFavorite>, response: Response<PostFavorite>) {
                        requestLikeGoods.value = response.body()
                        Log.i(TAG, "데이터 응답 메세지 : ${requestLikeGoods.value!!.message}")
                        Log.i(TAG, "데이터 응답 코드 : ${requestLikeGoods.value!!.code}")

                        Log.i(TAG, "가져온 데이터 : ${requestLikeGoods.value!!.result[0].favoriteIdx}")
                        dismissLoadingDialog()
                    }
                    override fun onFailure(call: Call<PostFavorite>, t: Throwable) {
//                        Log.e(TAG, "데이터 응답 메세지 : ${requestLikeGoods.value!!.message}")
                        Log.e(TAG, "데이터 응답 코드 : ${requestLikeGoods.value!!.code}")
                        Log.e(TAG, "onFailure: 실패", )
                        dismissLoadingDialog()
                    }
                })
        }

        showLoadingDialog(this)
        GetProductIdx.getSpecialGoods(jwt, data)
            .enqueue(object : Callback<GetSpecialGoodsData> {
                @SuppressLint("SetTextI18n")
                override fun onResponse(call: Call<GetSpecialGoodsData>, response: Response<GetSpecialGoodsData>) {
                    requestGetGoods.value = response.body()
                    Log.i(TAG, "데이터 응답 메세지 : ${requestGetGoods.value!!.message}")
                    Log.i(TAG, "데이터 응답 코드 : ${requestGetGoods.value!!.code}")

                    Log.d(TAG, "조회한 상품 IDX : ${requestGetGoods.value!!.result.productIdx}")
                    Log.d(TAG, "조회한 상품 제목 : ${requestGetGoods.value!!.result.title}")
                    binding.tvTitle.text = requestGetGoods.value!!.result.title
                    Log.d(TAG, "조회한 상품 내용 : ${requestGetGoods.value!!.result.contents}")
                    binding.tvContentsInfo.text = requestGetGoods.value!!.result.contents
                    Log.d(TAG, "조회한 상품 가격 : ${requestGetGoods.value!!.result.price}")
                    binding.tvPrice.text = requestGetGoods.value!!.result.price+"원"
                    Log.d(TAG, "조회한 상품 상태 : ${requestGetGoods.value!!.result.productStatus}")
                    when (requestGetGoods.value!!.result.productStatus) {
                        "U" -> binding.tvOldnew.text = "중고상품"
                        "N" -> binding.tvOldnew.text = "새상품"
                        else -> TODO()
                    }
                    Log.d(TAG, "조회한 상품 지역 : ${requestGetGoods.value!!.result.location}")
                    binding.tvLocation.text = requestGetGoods.value!!.result.location
                    Log.d(TAG, "조회한 상품 채팅 수 : ${requestGetGoods.value!!.result.chatCnt}")
                    binding.tvChat.text = requestGetGoods.value!!.result.chatCnt.toString()
                    Log.d(TAG, "조회한 상품 찜한 수 : ${requestGetGoods.value!!.result.favCnt}")
                    binding.tvLike.text = requestGetGoods.value!!.result.favCnt.toString()
                    Log.d(TAG, "조회한 상품 채팅 수 : ${requestGetGoods.value!!.result.createdAt}")
                    binding.tvDateTime.text = requestGetGoods.value!!.result.createdAt.toString()
                    Log.d(TAG, "조회한 상품 상품 갯수 : ${requestGetGoods.value!!.result.quantity}")
                    binding.tvHowmany.text = requestGetGoods.value!!.result.quantity.toString()
                    Log.d(TAG, "조회한 상품 교환 유무 : ${requestGetGoods.value!!.result.isChangable}")
                    when (requestGetGoods.value!!.result.isChangable){
                        true -> binding.tvChangable.text = "교환가능"
                        false -> binding.tvChangable.text = "교환불가"
                    }
                    Log.d(TAG, "조회한 상품 택배비 포함 유무 : ${requestGetGoods.value!!.result.isFreeShip}")
                    when (requestGetGoods.value!!.result.isFreeShip){
                        true -> binding.tvSendprice.text = "배송비포함"
                        false -> binding.tvSendprice.text = "배송비별도"
                    }
                }
                override fun onFailure(call: Call<GetSpecialGoodsData>, t: Throwable) {
                    Log.e(TAG, "데이터 응답 메세지 : ${requestGetGoods.value!!.message}")
                    Log.e(TAG, "데이터 응답 코드 : ${requestGetGoods.value!!.code}")
                }
            })

        GetProductIdx.getGoodsImage(jwt, data)
            .enqueue(object : Callback<GetGoodsImageAPI> {
                override fun onResponse(call: Call<GetGoodsImageAPI>, response: Response<GetGoodsImageAPI>) {
                    requestGetGoodsImage.value = response.body()

                    Log.e(TAG, "이미지 응답 메세지 : ${requestGetGoodsImage.value!!.message}")
                    Log.e(TAG, "이미지 응답 코드 : ${requestGetGoodsImage.value!!.code}")
                    Log.i(TAG, "이미지 조회하기 이미지 개수: ${requestGetGoodsImage.value!!.result.imageList.size}")
                    Log.d(TAG, "조회한 상품 IDX : ${requestGetGoodsImage.value!!.result.productIdx}")

                    val imgSize: Int = requestGetGoodsImage.value!!.result.imageList.size

                    binding.tvImageSize.text = imgSize.toString()

                    CoroutineScope(Dispatchers.Main).launch {
                        val bitmap = withContext(Dispatchers.IO) {
                            imageLoader.loadImage(requestGetGoodsImage.value!!.result.imageList[0])
                        }
                        binding.vpProductsImage.setImageBitmap(bitmap)
                        dismissLoadingDialog()
                    }
                }
                override fun onFailure(call: Call<GetGoodsImageAPI>, t: Throwable) {
                    Log.e(TAG, "이미지 응답 메세지 : ${requestGetGoodsImage.value!!.message}")
                    Log.e(TAG, "이미지 응답 코드 : ${requestGetGoodsImage.value!!.code}")
                    dismissLoadingDialog()
                }
            })
    }

    /** 특정 상품 삭제하기 */
    val deleteProducts : () -> Unit = {

        Log.i(TAG, "상품번호 : $data")
        Log.i(TAG, "회원 JWT : $jwt")

        val requestDeleteProducts = MutableLiveData<JustResponse>()

        GetProductIdx.deleteProducts(jwt, data)
            .enqueue(object : Callback<JustResponse> {
                override fun onResponse(call: Call<JustResponse>, response: Response<JustResponse>) {
                    requestDeleteProducts.value = response.body()

                    Log.e(TAG, "이미지 응답 메세지 : ${requestDeleteProducts.value!!.message}")
                    Log.e(TAG, "이미지 응답 코드 : ${requestDeleteProducts.value!!.code}")

                    dismissLoadingDialog()
                }
                override fun onFailure(call: Call<JustResponse>, t: Throwable) {
                    Log.e(TAG, "이미지 응답 메세지 : ${requestDeleteProducts.value!!.message}")
                    Log.e(TAG, "이미지 응답 코드 : ${requestDeleteProducts.value!!.code}")

                    dismissLoadingDialog()
                }
            })
    }
}