package com.alokomkar.starter.repository

import com.alokomkar.core.IoDispatcher
import com.alokomkar.starter.local.INewsLocalSource
import com.alokomkar.starter.local.NewsEntity
import com.alokomkar.starter.mapper.Mapper
import com.alokomkar.starter.remote.INewsRemoteSource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.flowOn
import javax.inject.Inject

interface INewsRepository {
    fun fetchNewsFromPage(page: Int = 1, hitsPerPage: Int = 20): Flow<List<NewsEntity>>
}

class NewsRepository @Inject constructor(
    private val localSource: INewsLocalSource,
    private val remoteSource: INewsRemoteSource,
    @IoDispatcher private val ioDispatcher: CoroutineDispatcher
): INewsRepository {

    override fun fetchNewsFromPage(page: Int, hitsPerPage: Int): Flow<List<NewsEntity>> = flow<List<NewsEntity>> {
        val localResponse = localSource.getAllNewsForPage(page)
        // TODO - update logic to be done
        if(localResponse.isEmpty()) {
            val response = remoteSource.fetchNews(page, hitsPerPage)
            val newLocalResponse = Mapper.convertToLocalList(response)
            localSource.updateNews(newsList = newLocalResponse)
            emit(newLocalResponse)
        }
        else emit(localResponse)
    }.flowOn(ioDispatcher)
}