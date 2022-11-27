package com.alokomkar.starter.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import kotlinx.coroutines.flow.Flow

@Dao
interface NewsDao {

    @Query("SELECT * FROM news_entity")
    fun getAllAsFlow(): Flow<List<NewsEntity>>

    @Query("SELECT * FROM news_entity WHERE page_ = :page")
    fun getAllFromPageAsFlow(page: Int): Flow<List<NewsEntity>>

    @Query("SELECT * FROM news_entity")
    fun getAll(): List<NewsEntity>

    @Query("SELECT * FROM news_entity WHERE page_ = :page")
    fun getAllForPage(page: Int): List<NewsEntity>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertAll(newsEntities: List<NewsEntity>)

    @Query("DELETE FROM news_entity")
    fun deleteAll()

    @Query("DELETE FROM news_entity WHERE page_ = :page")
    fun deleteAllForPage(page: Int)
}