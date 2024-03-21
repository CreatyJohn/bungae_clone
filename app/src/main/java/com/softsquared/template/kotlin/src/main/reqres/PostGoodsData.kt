package com.softsquared.template.kotlin.src.main.reqres

import java.sql.Timestamp
import java.util.Objects

/** 특정 회원조회 API */
// res
data class GetUserData (
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: GetUserDataResult
)

data class GetUserDataResult(
    val auth: Int,
    val safeCnt: Int,
    val createdAt: String,
    val followerCnt: Int,
    val followingCnt: Int,
    val name: String,
    val profileImg: Any,
    val rate: Double,
    val sellCnt: Int,
    val userIdx: Int,
    val introduce: String
)

/** 모든 카테고리 조회 API */
// res
data class GetCategoriesAPI (
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: List<GetCategoriesDataResult>
)

data class GetCategoriesDataResult(
    val mainCategory: Int,
    val mainCategoryName: String,
    val middleCategory: Int,
    val middleCategoryName: String,
    val subCategory: Int,
    val subCategoryName: String
)

/** 특정 회원에 대한 후기 조회 API */
// res
data class GetReviewData (
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: List<GetReviewDataResult>
)

data class GetReviewDataResult(
    val reviewIdx: Int,
    val rate: Double,
    val contents: String,
    val title: String,
    val name: String,
    val reviewerIdx: Int,
    val createdAt: Timestamp,
    val revieweeIdx: Int,
    val productIdx: Int
)

/** 특정 카테고리로 조회 API */
// res
data class GetCategoriesNumAPI (
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: List<GetCategoriesResult>
)

data class GetCategoriesResult(
    val productIdx: Int,
    val createdAt: Timestamp,
    val imageUrl: String,
    val title: String,
    val location: String,
    val price: String,
    val tradeStatus: String,
    val isFreeShip: Boolean,
    val isFav: Boolean,
    val isSafepay: Boolean
)

/** 특정 상품의 이미지 조회 API */
// res
data class GetGoodsImageAPI (
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: GetGoodsImageResult
)

data class GetGoodsImageResult (
    val productIdx: Int,
    val imageList: List<String>
)

/** 상품등록 API */
// req
data class PostGoodsData(
    val title: String,
    val price: String,
    val contents: String,
    val isSafepay: Boolean,
    val imageList: List<String>,
    val userIdx: Int,
    val mainCategoryIdx: Int,
    val middleCategoryIdx: Int,
    val subCategoryIdx: Int
)

// res
data class RequestPostGoods(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: ResultReqPostGoods
)

data class ResultReqPostGoods(
    val productIdx: Int
)

/** 상품 찜하기 & 찜해제 API */
data class PostFavorite(
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: List<ResultFavorite>
)

data class ResultFavorite(
    val favoriteIdx: Int
)

/** 상품조회 API */
data class GetGoodsData(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: List<ResultGoods>
)

data class ResultGoods(
    val productIdx: Int,
    val imageUrl: String,
    val isSafepay: Boolean,
    val title: String,
    val price: String,
    val location: String,
    val contents: String,
    val createdAt: Timestamp,
    val favCnt: Int,
    val isFav: Boolean
)

/** 상품조회 API */
data class GetSpecialGoodsData(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: ResultGetSpecialGoods
)

data class ResultGetSpecialGoods(
    val productIdx: Int,
    val price: String,
    val title: String,
    val location: String,
    val createdAt: Timestamp,
    val productStatus: String,
    val quantity: Int,
    val favCnt: Int,
    val chatCnt: Int,
    val contents: String,
    val isFreeShip: Boolean,
    val isChangable: Boolean
)

/** 토큰삽입 API */
data class ResultToken(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: Result
)

data class Result(
    val userIdx: Int,
    val jwt: String
)

/***/
data class MyName(
    val name: String
)

/** 소개글 API */
data class Introduce(
    val introduce: String
)

/** 상품 내용 수정 API */
data class EditProducts(
    val quantity: Int,
    val mainCategory: Int,
    val middleCategory: Int,
    val subCategory: Int,
    val title: String,
    val price: String,
    val location: String,
    val contents: String,
    val productStatus: String,
    val isChangable: Boolean,
    val isFreeShip: Boolean,
    val isSafePay: Boolean
)

/** 토큰삽입 API */
data class ResultJWT(
    val code: Int,
    val isSuccess: Boolean,
    val message: String,
    val result: JWTResult
)

data class JWTResult(
    val jwt: String,
    val userIdx: Int
)

/** 그냥 response만 받을때 */
data class JustResponse (
    val isSuccess: Boolean,
    val code: Int,
    val message: String,
    val result: String
)

/** response 에러 */
//data class ResponseError (
//    val timestamp: String,
//    val status: Int,
//    val error: String,
//    val message: String,
//    val path: String
//)
