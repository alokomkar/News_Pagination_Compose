package com.alokomkar.starter.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "news_entity")
data class NewsEntity(
    @PrimaryKey
    val createdAt: String,
    val author: String,
    val title: String,
    val webUrl: String,
    val page_: Int,
)