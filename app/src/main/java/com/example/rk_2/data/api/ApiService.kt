package com.example.rk_2.data.api

import com.example.rk_2.data.model.GiphyResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

interface GiphyApiService {
    @GET("v1/gifs/search")
    suspend fun searchGifs(
        @Query("api_key") apiKey: String,
        @Query("q") query: String,
        @Query("limit") limit: Int,
        @Query("offset") offset: Int,
        @Query("rating") rating: String = "g",  // Default value for rating
        @Query("lang") lang: String = "en",    // Default language
        @Query("bundle") bundle: String = "messaging_non_clips"
    ): Response<GiphyResponse>
}