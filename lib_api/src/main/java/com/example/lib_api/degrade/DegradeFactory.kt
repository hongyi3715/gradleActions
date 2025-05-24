package com.example.lib_api.degrade

import android.content.Context
import com.example.lib_annotation.degrade.IDegradeRegister

object DegradeFactory {

    private val interceptorCache = mutableMapOf<String, IRouteDegrade>()

    fun create(context: Context,name: String): IRouteDegrade{
        return interceptorCache.getOrPut(name) {
            val clazz = Class.forName(name)
            val instance = clazz.getDeclaredConstructor().newInstance() as IRouteDegrade
            instance
        }
    }
}