package com.alokomkar.starter.remote

import com.alokomkar.starter.remote.parser.NewsParser
import retrofit2.http.GET
import retrofit2.http.Query

interface NewsAPI {

    @GET("search")
    suspend fun getNews(
        @Query("query") query: String = "sports",
        @Query("page") page: Int = 1,
        @Query("hitsPerPage") hitsPerPage: Int = 20
    ): NewsParser
}