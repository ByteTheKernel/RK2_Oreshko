package com.example.rk_2.data.network

import com.example.rk_2.data.api.GiphyApiService
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
    const val BASE_URL = "https://api.giphy.com/"

    val apiService: GiphyApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(GiphyApiService::class.java)
    }
}