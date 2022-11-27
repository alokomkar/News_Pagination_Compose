package com.alokomkar.starter.local

import javax.inject.Inject

interface INewsLocalSource {
    suspend fun getAllNewsForPage(page: Int = 1): List<NewsEntity>
    suspend fun updateNews(newsList: List<NewsEntity>)
}

class NewsLocalSource @Inject constructor(
    private val dao: NewsDao
) : INewsLocalSource{

    override suspend fun getAllNewsForPage(page: Int): List<NewsEntity> {
        return dao.getAllForPage(page)
    }

    override suspend fun updateNews(newsList: List<NewsEntity>) {
       dao.insertAll(newsList)
    }
}