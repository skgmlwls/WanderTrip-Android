package com.lion.wandertrip.retrofit

import com.google.gson.GsonBuilder
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

object RetrofitClient {
    private const val BASE_URL = "https://apis.data.go.kr/B551011/KorService1/"
    var gson= GsonBuilder().setLenient().create()

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .build()
    }

    val apiService: PublicDataApiService by lazy {
        retrofit.create(PublicDataApiService::class.java)
    }
}
