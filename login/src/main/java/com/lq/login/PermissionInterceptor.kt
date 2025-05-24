package com.lq.login

import android.content.Context
import com.example.lib_annotation.RouterInterceptor
import com.example.lib_api.interceptor.IRouteInterceptor
import com.example.lib_api.interceptor.InterceptorChain

@RouterInterceptor
class PermissionInterceptor: IRouteInterceptor {
    override fun init(context: Context) {

    }

    override suspend fun proceed(chain: InterceptorChain) {
        var permissionGranted = true
        if(permissionGranted){
            chain.proceed()
        }else{
            chain.intercept("没有权限")
        }

    }
}