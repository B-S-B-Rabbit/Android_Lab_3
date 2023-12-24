package com.example.androidlab3

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsApi {
    @GET("news")
    fun getNews(@Query("q") query: String, @Query("apikey") apiKey: String): Call<NewsItem>
}

