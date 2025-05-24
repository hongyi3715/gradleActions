package com.example.lib_api.interceptor

import android.content.Context

interface InterceptorChain {
    val path: String
    val context: Context
    fun intercept(reason: String)
    suspend fun proceed()
}


