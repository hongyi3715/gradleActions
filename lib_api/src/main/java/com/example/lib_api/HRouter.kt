package com.example.lib_api

import android.app.Application
import com.example.lib_api.autowired.AutoWiredHelper
import com.example.lib_api.route.RouteHelper

object HRouter {

    private lateinit var app: Application

    fun init(context: Application){
        app = context
        RouteHelper.findRoot()
    }


    fun build(path: String): HRouterDelegate {
        return HRouterDelegate(path).withContext(app)
    }

    fun inject(target: Any){
        AutoWiredHelper.inject(target)
    }

}