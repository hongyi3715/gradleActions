package com.example.lib_api

import android.app.Application
import com.example.lib_annotation.data.InterceptorMeta
import com.example.lib_api.autowired.AutoWiredHelper
import com.example.lib_api.deeplink.DeepLinkManager
import com.example.lib_api.degrade.DegradeManager
import com.example.lib_api.exception.UriParseIllegalException
import com.example.lib_api.interceptor.InterceptorManager
import com.example.lib_api.route.RouteHelper

object HRouter {

    //todo 1.支持更多参数， 2 deeplink 3 打包aar 4 注入参数的健壮性 5 loadService

    private lateinit var app: Application

    fun init(context: Application){
        app = context
        RouteHelper.findRoot()
        InterceptorManager.initInterceptor(context)
        DegradeManager.init(context)
        DeepLinkManager.init(context)
    }


    fun build(path: String): HRouterDelegate {
        return HRouterDelegate(path).withContext(app)
    }

    fun buildUri(uri:String): HRouterDelegate{
        val path = DeepLinkManager.getPathFromUri(uri)
        if(path.isNullOrEmpty())  throw UriParseIllegalException(uri)
        return HRouterDelegate(path).withContext(app)
    }

    fun inject(target: Any){
        AutoWiredHelper.inject(target)
    }

}