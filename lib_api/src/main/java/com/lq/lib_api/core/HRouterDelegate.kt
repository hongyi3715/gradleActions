package com.lq.lib_api.core

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.lifecycleScope
import com.lq.lib_annotation.data.RouteMeta
import com.lq.lib_api.autowired.ParameterBuilder
import com.lq.lib_api.interceptor.InterceptorManager
import com.lq.lib_api.route.RouteHelper
import com.lq.lib_api.util.LogUtil
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.launch

internal class HRouterDelegate (path: String){
    private var routeMeta: RouteMeta = RouteHelper.findGroup(path)
    private val bundle = Bundle()
    private lateinit var context: Context
    private var launcher: ActivityResultLauncher<Intent>? = null
    private var enterAnim: Int? = null
    private var exitAnim: Int? = null
    private val intentBuilder : IntentBuilder by lazy { buildIntent() }

    fun withParams(block: ParameterBuilder.()-> Unit): HRouterDelegate{
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
        LogUtil.d("Navigate Context:${context}")
        if((context is LifecycleOwner) ){
            (context as LifecycleOwner).lifecycleScope.launch {
                interceptHandle()
            }
        }else{
            CoroutineScope(SupervisorJob() +Dispatchers.Default).launch {
                interceptHandle()
            }
        }
    }

    private suspend fun interceptHandle(){
        val path = intentBuilder.getPath()
        val ctx = context
        if(InterceptorManager.handleInterceptor(path,ctx)){
            startActivity()
        }
    }
}