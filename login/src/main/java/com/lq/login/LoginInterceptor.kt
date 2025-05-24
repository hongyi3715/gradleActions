package com.lq.login

import android.content.Context
import com.example.lib_annotation.RouterInterceptor
import com.example.lib_api.HRouter
import com.example.lib_api.interceptor.IRouteInterceptor
import com.example.lib_api.interceptor.InterceptorChain
import com.example.lib_api.util.LogUtil
import com.example.lib_api.util.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@RouterInterceptor
class LoginInterceptor: IRouteInterceptor {
    override val whiteList: Set<String>
        get() = setOf("/login/login")
    override fun init(context: Context) {

    }

    override suspend fun proceed(chain: InterceptorChain) {
        withContext(Dispatchers.IO) {
            delay(1000)
            if (LoginManager.isLogin) {
                chain.proceed()
            } else {
                LogUtil.d("未登录 path:${chain.path}")
                withContext(Dispatchers.Main){
                    showToast(chain.context,"未登录 path:${chain.path}")
                    HRouter.build("/login/login").navigate()
                    chain.intercept("未登录")
                }
            }
        }
    }
}