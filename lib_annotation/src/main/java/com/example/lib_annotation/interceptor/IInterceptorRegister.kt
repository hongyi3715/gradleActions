package com.example.lib_annotation.interceptor

import com.example.lib_annotation.data.InterceptorMeta

interface IInterceptorRegister {

    fun register(list: MutableList<InterceptorMeta>)
}