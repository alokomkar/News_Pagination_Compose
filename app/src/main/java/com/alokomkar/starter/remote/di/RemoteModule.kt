package com.alokomkar.starter.remote.di

import android.content.Context
import com.alokomkar.starter.remote.NewsAPI
import com.alokomkar.starter.remote.cache.CacheInterceptor
import com.alokomkar.starter.remote.cache.ForceCacheInterceptor
import com.alokomkar.starter.utils.INetworkUtilsWrapper
import com.alokomkar.starter.utils.NetworkUtilsWrapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import okhttp3.Cache
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

@Module
@InstallIn(SingletonComponent::class)
class RemoteModule {

    private val baseUrl = "https://hn.algolia.com/api/v1/"

    @Provides
    fun provideNewsAPI(retrofit: Retrofit): NewsAPI {
        return retrofit.create(NewsAPI::class.java)
    }

    @Provides
    fun providesRetrofit(
        okHttpClient: OkHttpClient,
    ): Retrofit {
        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    fun provideOkhttpClient(
        @ApplicationContext context: Context, 
        networkUtilsWrapper: INetworkUtilsWrapper
    ): OkHttpClient
        = OkHttpClient.Builder()
        .cache(Cache(context.cacheDir, 5 * 1024 * 1024))
        .addNetworkInterceptor(CacheInterceptor())
        .addInterceptor(ForceCacheInterceptor(networkUtilsWrapper))
        .addInterceptor { interceptorChain ->
            val request = interceptorChain.request().newBuilder().build()
            interceptorChain.proceed(request)
        }.build()

    @Provides
    fun providesNetworkUtilsWrapper(@ApplicationContext context: Context): INetworkUtilsWrapper {
        return NetworkUtilsWrapper(context)
    }

//    @Singleton
//    @Provides
//    fun provideMoshi(): Moshi
//            = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
//
//    @Provides
//    fun provideMoshiConverterFactory(moshi: Moshi): MoshiConverterFactory
//        = MoshiConverterFactory
//        .create(moshi)

}