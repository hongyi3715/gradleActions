package com.example.lib_api.interceptor

import java.util.Collections

object InterceptorHistory {
    private val pathStack = Collections.synchronizedSet(LinkedHashSet<String>())


    fun push(path:String):Boolean {
        if(pathStack.contains(path)) return false
        pathStack.add(path)
        return true
    }

    fun pop(path: String){
        pathStack.remove(path)
    }

    fun hasVisited(path:String):Boolean  = pathStack.contains(path)

    fun clear() = pathStack.clear()

}