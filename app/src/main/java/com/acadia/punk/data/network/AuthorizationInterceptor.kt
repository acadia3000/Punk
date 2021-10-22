package com.acadia.punk.data.network

import okhttp3.Interceptor
import okhttp3.Interceptor.Chain
import okhttp3.Response

class AuthorizationInterceptor() : Interceptor {

    private val apiKey = "716cf57f17a442a1a2055ab61d18818c"

    override fun intercept(chain: Chain): Response =
        chain.proceed(chain.request().newBuilder().apply {
            addHeader("X-Api-Key", apiKey)
        }.build())
}
