package com.lq.lib_api.core

import android.app.Activity
import android.app.ActivityOptions
import android.app.Application
import android.content.Context
import android.content.Intent
import android.os.Bundle
import com.lq.lib_annotation.data.RouteMeta
import com.lq.lib_api.degrade.DegradeManager
import com.lq.lib_api.exception.RouteMetaIllegalException

internal class IntentBuilder (private val context: Context) {
    private val intent = Intent()
    private lateinit var routeMeta: RouteMeta

    fun flags(flag: Int){
        intent.addFlags(flag)
    }

    fun set(meta: RouteMeta){
        routeMeta =meta
        intent.setClass(context,meta.destination)
    }

    fun getPath(): String{
         if(!this::routeMeta.isInitialized) throw RouteMetaIllegalException()
        return routeMeta.path
    }

    fun get() = intent

    fun put(bundle:Bundle?){
        bundle?:return
        intent.putExtras(bundle)
    }

    fun startActivity(){
        try {
            if(context is Application){
                flags(Intent.FLAG_ACTIVITY_NEW_TASK)
            }
            context.startActivity(intent)
        }catch (e: Exception){
            e.printStackTrace()
            DegradeManager.handleDegrade(routeMeta.path,e.message ?:"UnKnow",context)
        }
    }


    fun startWithAnim(activity: Activity, enterAnim: Int, exitAnim: Int) {
        val options = ActivityOptions.makeCustomAnimation(context, enterAnim, exitAnim)
        activity.startActivity(intent, options.toBundle())
    }

}