package com.softsquared.template.kotlin.util

object API {

    const val BASE_URL: String = "https://harry-domain.shop/bunjang/"

    const val GET_SPECIAL_PRODUCTS: String = "products/{productIdx}"
    const val GET_PRODUCTS: String = "products"
    const val GET_PRODUCTS_IMAGE: String = "products/{productIdx}/images"
    const val GET_USER_DATA: String = "users/{userIdx}"
    const val GET_REVIEW_DATA: String = "users/{userIdx}/reviews"
    const val GET_CATEGORIES_DATA: String = "categories"
    const val GET_CATEGORIES_NUM_DATA: String = "categories/{categoryNum}"
    const val POST_NEW_TOKEN: String = "users/sign-up/naver"
    const val POST_LOGIN_TOKEN: String = "users/log-in/naver"
    const val POST_GOODS_DATA: String = "products/new"
    const val POST_FAVORITE_PRODUCT: String = "products/{productIdx}/favorites"
    const val DELETE_USER_DATA: String = "users/{userIdx}/d"
    const val PATCH_USER_DATA: String = "users/{userIdx}/name"
    const val PATCH_STORE_INFO: String = "users/{userIdx}/introduce"
    const val PATCH_EDIT_PRODUCTS: String = "products/{productIdx}/edit"
    const val DELETE_PRODUCTS: String = "products/{productIdx}/d"
}

const val TAG = "BUNGGE"

// 번개장터.test 안드로이드 디바이스에 부여된 토큰 값
const val uid = "eRglclxDQN-WJXBo4HAcr4:APA91bGno98-2r-kqxMqndmLLAKKGRLtd-VKZXNjOHpQiiD8oN_sUNWyOf802pgZ5Jqc2ZOSHC_4B3eyF_57nBbvDKcjIZPuzZbKOfDbgAPgyDMWCzuHxG3QM9zcOhtg9qwN2VjZ-A6G"

object ETC {
    const val REQUEST_FIRST = 1000
    const val REQUEST_GET_IMAGE = 2000
}

class DBkey {
    companion object {
        const val DB_ARTICLE = "Article"
    }
}