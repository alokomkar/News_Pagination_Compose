package com.alokomkar.starter.remote.cache

import com.alokomkar.starter.utils.INetworkUtilsWrapper
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.Request
import okhttp3.Response

class ForceCacheInterceptor(
    private val networkUtilsWrapper: INetworkUtilsWrapper
) : Interceptor {

    override fun intercept(chain: Interceptor.Chain): Response {
        val builder: Request.Builder = chain.request().newBuilder()
        if (!networkUtilsWrapper.isNetworkAvailable()) {
            builder.cacheControl(CacheControl.FORCE_CACHE);
        }
        return chain.proceed(builder.build());
    }

}