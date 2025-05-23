package com.example.lib_api

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.example.lib_annotation.data.RouteMeta
import com.example.lib_api.autowired.ParameterBuilder
import com.example.lib_api.interceptor.InterceptorManager
import com.example.lib_api.interceptor.TestInterceptor
import com.example.lib_api.route.RouteHelper
import com.example.lib_api.util.LogUtil
import kotlinx.coroutines.launch

class HRouterDelegate (path: String){
    private var routeMeta: RouteMeta = RouteHelper.findGroup(path)
    private val bundle = Bundle()
    private lateinit var context: Context
    private var launcher: ActivityResultLauncher<Intent>? = null
    private var enterAnim: Int? = null
    private var exitAnim: Int? = null
    private val intentBuilder : IntentBuilder by lazy { buildIntent() }

    fun with(block: ParameterBuilder.()-> Unit): HRouterDelegate{
        bundle.putAll(ParameterBuilder().apply(block).bundle)
        return this
    }

    fun withContext(context: Context): HRouterDelegate{
        this.context = context
        return this
    }

    fun withAnim(enter:Int,exit: Int): HRouterDelegate{
        this.enterAnim = enter
        this.exitAnim = exit
        return this
    }
    private fun buildIntent(): IntentBuilder{
        return IntentBuilder(context).apply{
            set(routeMeta)
            put(bundle)
            if (context is Application) flags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
    }

    private fun startActivity(){
        when {
            launcher != null -> launcher?.launch(intentBuilder.get())
            context is Activity && enterAnim != null && exitAnim != null ->
                intentBuilder.startWithAnim(context as Activity, enterAnim!!, exitAnim!!)
            else -> intentBuilder.startActivity()
        }
    }

    fun navigate(){
        if((context is LifecycleOwner) ){
            (context as LifecycleOwner).lifecycleScope.launch {
                InterceptorManager.addInterceptor(TestInterceptor())
                val intercepted = InterceptorManager.handleInterceptor(intentBuilder.getPath(),context)
                LogUtil.d("Route Intercept :${intentBuilder.getPath()} has intercepted ? :$intercepted")
                if(intercepted){
                    startActivity()
                }
            }
        }
    }
}