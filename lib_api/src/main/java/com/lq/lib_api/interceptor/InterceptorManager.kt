package com.lq.lib_api.interceptor

import android.content.Context
import com.lq.lib_annotation.data.InterceptorMeta
import com.lq.lib_annotation.interceptor.IInterceptorRegister
import com.lq.lib_api.util.LogUtil
import kotlin.collections.forEach

//todo 动态参数
internal object InterceptorManager {
    private val interceptors = mutableListOf<IRouteInterceptor>()

    private val data = mutableListOf<InterceptorMeta>()

    private val globalWhiteList = mutableSetOf<String>()

    fun initInterceptor(context: Context){
        val clazz = Class.forName("com.lq.router.InterceptorIndex")
        val instance = clazz.getField("INSTANCE").get(null) // 拿到 object 的单例实例
        val method = clazz.getDeclaredMethod("getRoots")

        val registers = method.invoke(instance) as List<IInterceptorRegister>

        registers.forEach {
           it.register(data)
        }
        data.sortedBy { it.priority }.toMutableList().forEach {
            InterceptorFactory.create(context,it.destination).apply {
                addInterceptor(this)
            }
        }
    }


    fun addInterceptor(intercept: IRouteInterceptor){
        interceptors.add(intercept)
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

    private fun isFromGroup(path:String,group:String):Boolean{
        return true
    }

    private fun isInWhiteList(path: String,route: IRouteInterceptor) :Boolean{
        return globalWhiteList.contains(path) || route.whiteList.contains(path)
    }


}