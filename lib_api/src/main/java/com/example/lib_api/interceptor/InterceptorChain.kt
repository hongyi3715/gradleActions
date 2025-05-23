package com.example.lib_api.interceptor

import android.content.Context

interface InterceptorChain {
    val path: String
    val context: Context
    suspend fun proceed()
    fun intercept(reason: String)
}