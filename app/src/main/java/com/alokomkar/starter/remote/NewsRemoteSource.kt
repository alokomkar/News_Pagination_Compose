package com.alokomkar.starter.remote

import com.alokomkar.starter.remote.parser.NewsParser
import javax.inject.Inject

interface INewsRemoteSource {
    suspend fun fetchNews(
        page: Int,
        hitsPerPage: Int
    ): NewsParser
}

class NewsRemoteSource @Inject constructor(
    private val api: NewsAPI
) : INewsRemoteSource{

    override suspend fun fetchNews(
        page: Int,
        hitsPerPage: Int
    ): NewsParser = api.getNews(page = page, hitsPerPage = hitsPerPage)
}