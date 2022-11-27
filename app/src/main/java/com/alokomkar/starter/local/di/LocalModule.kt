package com.alokomkar.starter.local.di

import android.content.Context
import androidx.room.Room
import com.alokomkar.starter.local.NewsDao
import com.alokomkar.starter.local.NewsDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class LocalModule {
    private val databaseName = "news.db"

    @Singleton
    @Provides
    fun providesNewsDao(database: NewsDatabase): NewsDao = database.newsDao()

    @Singleton
    @Provides
    internal fun provideDatabase(@ApplicationContext context: Context): NewsDatabase {
        return Room.databaseBuilder(context, NewsDatabase::class.java, databaseName)
            .fallbackToDestructiveMigration()
            .build()
    }
}