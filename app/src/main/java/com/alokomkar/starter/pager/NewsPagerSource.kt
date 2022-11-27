package com.alokomkar.starter.pager

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.alokomkar.starter.local.NewsEntity
import com.alokomkar.starter.repository.INewsRepository
import kotlinx.coroutines.flow.last

class NewsPagerSource(
    private val repository: INewsRepository
): PagingSource<Int, NewsEntity>() {

    override fun getRefreshKey(state: PagingState<Int, NewsEntity>): Int? {
        return state.anchorPosition?.let { position ->
            val page = state.closestPageToPosition(position)
            page?.prevKey?.minus(1) ?: page?.nextKey?.plus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, NewsEntity> {
        return try {
            val page = params.key ?: 1
            val response = repository.fetchNewsFromPage(page, 20)
            val result = response.last().toList()
            LoadResult.Page(
                data = result,
                prevKey = null,
                nextKey = if(result.isNotEmpty()) result.last().page_ + 1 else null
            )
        } catch (e: Exception) {
            LoadResult.Error(e)
        }
    }
}