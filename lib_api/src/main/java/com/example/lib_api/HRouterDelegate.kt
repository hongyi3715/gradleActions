package com.example.lib_api

import android.app.Activity
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.result.ActivityResultLauncher
import com.example.lib_annotation.data.RouteMeta
import com.example.lib_api.autowired.ParameterBuilder
import com.example.lib_api.route.RouteHelper

class HRouterDelegate (path: String){
     private var routeMeta: RouteMeta = RouteHelper.findGroup(path)
    private val bundle = Bundle()
    private lateinit var context: Context
    private var launcher: ActivityResultLauncher<Intent>? = null
    private var enterAnim: Int? = null
    private var exitAnim: Int? = null

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

    fun navigate(){
        val intentBuilder = IntentBuilder(context).apply{
            set(routeMeta.destination)
            put(bundle)
            if (context is Application) flags(Intent.FLAG_ACTIVITY_NEW_TASK)
        }
        when {
            launcher != null -> launcher?.launch(intentBuilder.get())
            context is Activity && enterAnim != null && exitAnim != null ->
                intentBuilder.startWithAnim(context as Activity, enterAnim!!, exitAnim!!)
            else -> intentBuilder.startActivity()
        }
    }
}