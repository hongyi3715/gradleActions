package com.example.lib_api

import android.util.Log
import com.example.lib_annotation.register.IRouteRoot

object HRouter {
    private val routeRoot = mutableMapOf<String,String>()

    fun init(){
        val rootClazz = Class.forName("com.lq.router.Root")
        val root = rootClazz.getDeclaredConstructor().newInstance() as IRouteRoot
        root.loadInto(routeRoot)
        routeRoot.forEach {
            Log.d("MyRoot","info: $it")
        }
    }
}