package com.lq.lib_api.interceptor

import android.content.Context

internal object InterceptorFactory {
    private val interceptorCache = mutableMapOf<String, IRouteInterceptor>()

    fun create(context: Context,name: String): IRouteInterceptor{
        return interceptorCache.getOrPut(name) {
            val clazz = Class.forName(name)
            val instance = clazz.getDeclaredConstructor().newInstance() as IRouteInterceptor
            instance.init(context)
            instance
        }
    }
}