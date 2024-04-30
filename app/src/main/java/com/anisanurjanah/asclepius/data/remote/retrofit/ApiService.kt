package com.anisanurjanah.asclepius.data.remote.retrofit

import com.anisanurjanah.asclepius.data.remote.response.ArticleResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("top-headlines")
    fun getTopHeadlines(
        @Query("q") query: String,
        @Query("category") category: String,
        @Query("language") language: String
    ): Call<ArticleResponse>
}