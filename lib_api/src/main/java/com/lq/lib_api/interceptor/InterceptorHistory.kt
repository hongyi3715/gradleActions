package com.lq.lib_api.interceptor

import com.lq.lib_api.util.LogUtil
import java.util.Collections

internal object InterceptorHistory {
    private val pathStack = Collections.synchronizedSet(LinkedHashSet<String>())

    private const val MAX_STACK = 4

    fun push(path:String):Boolean {
        if(pathStack.contains(path)) {
            LogUtil.d("重复跳转 path:$path")
            return false
        }
        pathStack.add(path)
        return true
    }

    fun pop(path: String){
        pathStack.remove(path)
    }

    fun hasVisited(path:String):Boolean  = pathStack.contains(path)

    fun clear() = pathStack.clear()

}