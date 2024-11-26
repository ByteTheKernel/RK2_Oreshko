package com.example.rk_2.repository

import com.example.rk_2.BuildConfig
import com.example.rk_2.data.network.RetrofitClient
import com.example.rk_2.data.model.GiphyResponse


import retrofit2.Call
import retrofit2.Response

class GiphyRepository  {

    private val apiService = RetrofitClient.apiService

    suspend fun searchGifs(query: String, limit: Int, offset: Int): Response<GiphyResponse> {
        return apiService.searchGifs("QrLOHlOKP1gOFS2gQ9fC0RynfnAJS9T0", query, limit, offset)
    }
}