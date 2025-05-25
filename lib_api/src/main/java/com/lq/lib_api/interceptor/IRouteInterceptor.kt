package com.lq.lib_api.interceptor

import android.content.Context


interface IRouteInterceptor {

    val whiteList :Set<String> get() = emptySet()

    fun init(context: Context)

    suspend fun proceed(chain: InterceptorChain)
}

