package com.example.lib_api.interceptor


import android.content.Context
import com.example.lib_annotation.RouterInterceptor
import com.example.lib_api.HRouter
import com.example.lib_api.util.showToast
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.withContext

@RouterInterceptor(priority = 1)
class TestInterceptor : IRouteInterceptor{
    override fun init(context: Context) {

    }

    override suspend fun proceed(chain: InterceptorChain) {
        withContext(Dispatchers.IO){
            delay(1000)
            var mockIsLogin = false
            if(mockIsLogin){
                chain.proceed()
            }else{
                withContext(Dispatchers.Main){
                    showToast(chain.context,"未登录")
                    chain.intercept("未登录")
                }

            }
        }
    }
}