package com.softsquared.template.kotlin.src.main.reqres

import com.softsquared.template.kotlin.util.API
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

//싱글턴
object RetrofitClient {

    private val retrofit: Retrofit.Builder by lazy {
        Retrofit.Builder()
            .baseUrl(API.BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
    }

    val apiService: IRetrofit by lazy {
        retrofit
            .build()
            .create(IRetrofit::class.java)
    }
}