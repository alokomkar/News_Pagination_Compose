package com.alokomkar.starter.repository.di

import com.alokomkar.starter.local.INewsLocalSource
import com.alokomkar.starter.local.NewsLocalSource
import com.alokomkar.starter.local.di.LocalModule
import com.alokomkar.starter.remote.INewsRemoteSource
import com.alokomkar.starter.remote.NewsRemoteSource
import com.alokomkar.starter.remote.di.RemoteModule
import com.alokomkar.starter.repository.INewsRepository
import com.alokomkar.starter.repository.NewsRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@Module(includes = [RemoteModule::class, LocalModule::class])
@InstallIn(SingletonComponent::class)
interface RepositoryModule {

    @Binds
    fun bindRemoteSource(remoteSource: NewsRemoteSource): INewsRemoteSource

    @Binds
    fun bindLocalSource(localSource: NewsLocalSource): INewsLocalSource

    @Binds
    fun bindRepository(repository: NewsRepository): INewsRepository


}