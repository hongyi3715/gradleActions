package com.example.lib_api.interceptor

import android.content.Context
import com.example.lib_api.util.LogUtil

//todo 动态参数
object InterceptorManager {
    private val interceptors = mutableListOf<IRouteInterceptor>()

    private val globalWhiteList = mutableSetOf<String>()


    fun addInterceptor(intercept: IRouteInterceptor){
        interceptors.add(intercept)
    }

    fun addAllInterceptor(data:List<IRouteInterceptor>){
        interceptors.addAll(data)
    }

    fun init(context: Context){
        interceptors.forEach {
            it.init(context)
        }
    }

    suspend fun handleInterceptor(path: String,context: Context): Boolean{
        val iterators = interceptors.toList().iterator()

        var isIntercepted = false

        if(!InterceptorHistory.push(path)) return false


        val chain = object:InterceptorChain{
            override val path = path
            override val context = context

            override suspend fun proceed() {
                if (isIntercepted) return
                if (!iterators.hasNext()) return
                val interceptor = iterators.next()
                if (isInWhiteList(path, interceptor)) {
                    proceed()
                } else {
                    interceptor.proceed(this)
                }
            }

            override fun intercept(reason: String) {
                isIntercepted = true
                LogUtil.d("Route Intercept Reason :$reason")
            }

        }
       try {
           chain.proceed()
       }finally {
           InterceptorHistory.pop(path)
       }
        return !isIntercepted
    }

    private fun isInWhiteList(path: String,route: IRouteInterceptor) :Boolean{
        return globalWhiteList.contains(path) || route.whiteList.contains(path)
    }
}