package com.softsquared.template.kotlin.src.main.reqres

import com.softsquared.template.kotlin.util.API
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.PATCH
import retrofit2.http.POST
import retrofit2.http.Query
import retrofit2.http.Path

interface IRetrofit {

    /** Keyword 로 Regist 한 Goods 들을 Inquire 하는 Get request API */
    @GET(API.GET_PRODUCTS)
    fun getGoods(
        @Header("X-ACCESS-TOKEN") jwt: String, // 호출을 액세스 토큰으로 할 시에 사용되는 것
        @Query("keyword") keyword: String
    ) : Call<GetGoodsData>

    /** Regist 한 모든 Goods 들을 Inquire 하는 Get request API */
    @GET(API.GET_PRODUCTS)
    fun getAllGoods(
        @Header("X-ACCESS-TOKEN") jwt: String, // 호출을 액세스 토큰으로 할 시에 사용되는 것
    ) : Call<GetGoodsData>

    /** 특정 상품을 조회하는 Get request API */
    @GET(API.GET_SPECIAL_PRODUCTS)
    fun getSpecialGoods(
        @Header("X-ACCESS-TOKEN") jwt: String, // 호출을 액세스 토큰으로 할 시에 사용되는 것
        @Path("productIdx") productIdx: Int
    ) : Call<GetSpecialGoodsData>

    /** 특정 상품의 이미지 조회하기 */
    @GET(API.GET_PRODUCTS_IMAGE)
    fun getGoodsImage(
        @Header("X-ACCESS-TOKEN") jwt: String, // 호출을 액세스 토큰으로 할 시에 사용되는 것
        @Path("productIdx") productIdx: Int
    ) : Call<GetGoodsImageAPI>

    /** UserIdx 로 특정 User 를 Inquire 하는 GET request API */
    @GET(API.GET_USER_DATA)
    fun getUserData(
        @Header("X-ACCESS-TOKEN") jwt: String, // 호출을 액세스 토큰으로 할 시에 사용되는 것
        @Path("userIdx") userIdx: Int
    ) : Call<GetUserData>

    /** 전체 Categories 조회하기 */
    @GET(API.GET_CATEGORIES_DATA)
    fun getCategoriesData(
        @Header("X-ACCESS-TOKEN") jwt: String, // 호출을 액세스 토큰으로 할 시에 사용되는 것
    ) : Call<GetCategoriesAPI>

    /** 전체 Categories 조회하기 */
    @GET(API.GET_CATEGORIES_NUM_DATA)
    fun getCategoriesDataNum(
        @Header("X-ACCESS-TOKEN") jwt: String, // 호출을 액세스 토큰으로 할 시에 사용되는 것
        @Path("categoryNum") categoryNum: Int
    ) : Call<GetCategoriesNumAPI>

    /** 특정 회원에 대한 후기 조회하기 */
    @GET(API.GET_REVIEW_DATA)
    fun getReviewData(
        @Header("X-ACCESS-TOKEN") jwt: String, // 호출을 액세스 토큰으로 할 시에 사용되는 것
        @Path("userIdx") userIdx: Int
    ) : Call<GetReviewData>



    /** NAVER_ACCESSTOKEN 을 POST request 하여 회원가입 하는 API */
    @POST(API.POST_NEW_TOKEN)
    fun postNaverToken(
        @Header("NAVER-ACCESS-TOKEN") naverAccessToken: String,
        @Body name: MyName
    ) : Call<ResultToken>

    /** NAVER_ACCESSTOKEN 을 POST request 하여 JWT 및 UserIdx 를 Response 하는 API : 로그인 API */
    @POST(API.POST_LOGIN_TOKEN)
    fun postNaverLoginToken(
        @Header("NAVER-ACCESS-TOKEN") naverAccessToken: String
    ) : Call<ResultJWT>

    /** New Goods 들을 Regist 하기 위해 Post request 하는 API */
    @POST(API.POST_GOODS_DATA)
    fun postGoodsData(
        @Header("X-ACCESS-TOKEN") jwt: String, // 호출을 액세스 토큰으로 할 시에 사용되는 것
        @Body postgoodsdata: PostGoodsData
    ) : Call<RequestPostGoods>

    /** 찜하기 or 찜 해제 기능의 API */
    @POST(API.POST_FAVORITE_PRODUCT)
    fun postProductFav(
        @Header("X-ACCESS-TOKEN") jwt: String, // 호출을 액세스 토큰으로 할 시에 사용되는 것
        @Path("productIdx") productIdx: Int
    ) : Call<PostFavorite>



    /** regist 된 user 의 UserIdx 를 가지고 정보를 완전히 Delete : 회원탈퇴 */
    @PATCH(API.DELETE_USER_DATA)
    fun deleteUserData(
        @Header("X-ACCESS-TOKEN") jwt: String, // 호출을 액세스 토큰으로 할 시에 사용되는 것
        @Path("userIdx") userIdx: Int
    ) : Call<JustResponse>

    /** regist 된 user 의 UserIdx 를 가지고 정보를 수정 */
    @PATCH(API.PATCH_USER_DATA)
    fun patchUserData(
        @Header("X-ACCESS-TOKEN") jwt: String, // 호출을 액세스 토큰으로 할 시에 사용되는 것
        @Body name: MyName,
        @Path("userIdx") userIdx: Int
    ) : Call<JustResponse>

    /** regist 된 user 의 UserIdx 를 가지고 정보를 수정 */
    @PATCH(API.PATCH_STORE_INFO)
    fun patchStoreInfoData(
        @Header("X-ACCESS-TOKEN") jwt: String, // 호출을 액세스 토큰으로 할 시에 사용되는 것
        @Body introduce: Introduce,
        @Path("userIdx") userIdx: Int
    ) : Call<JustResponse>

    /** 상품 내용 수정 API */
    @PATCH(API.PATCH_EDIT_PRODUCTS)
    fun patchEditProducts(
        @Header("X-ACCESS-TOKEN") jwt: String, // 호출을 액세스 토큰으로 할 시에 사용되는 것
        @Body editProducts: EditProducts,
        @Path("productIdx") productIdx: Int
    ) : Call<JustResponse>

    

    /** 특정 상품 삭제 */
    @DELETE(API.PATCH_EDIT_PRODUCTS)
    fun deleteProducts(
        @Header("X-ACCESS-TOKEN") jwt: String, // 호출을 액세스 토큰으로 할 시에 사용되는 것
        @Path("productIdx") productIdx: Int
    ) : Call<JustResponse>
}