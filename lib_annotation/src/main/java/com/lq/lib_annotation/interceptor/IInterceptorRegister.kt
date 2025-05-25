package com.lq.lib_annotation.interceptor

import com.lq.lib_annotation.data.InterceptorMeta

interface IInterceptorRegister {

    fun register(list: MutableList<InterceptorMeta>)
}