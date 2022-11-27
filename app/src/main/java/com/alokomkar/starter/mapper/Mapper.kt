package com.alokomkar.starter.mapper

import com.alokomkar.starter.local.NewsEntity
import com.alokomkar.starter.remote.parser.NewsParser

object Mapper {
    fun convertToLocalList(newsParser: NewsParser): List<NewsEntity> {
        val newsList = mutableListOf<NewsEntity>()
        newsParser.hits.forEach {
            newsList.add(
                NewsEntity(
                    createdAt = it.createdAt,
                    title = it.title,
                    author = it.author,
                    webUrl = it.url ?: "",
                    page_ = newsParser.page
                )
            )
        }
        return newsList
    }
}