package com.example.lib_api

import android.app.Activity
import android.app.ActivityOptions
import android.app.Application
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.Intent
import android.os.Bundle

class IntentBuilder (private val context: Context) {
    private val intent = Intent()

    fun flags(flag: Int){
        intent.addFlags(flag)
    }

    fun set(clazz:Class<*>){
        intent.setClass(context,clazz)
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
        }catch (e: ActivityNotFoundException){
            e.printStackTrace()
        }
    }


    fun startWithAnim(activity: Activity, enterAnim: Int, exitAnim: Int) {
        val options = ActivityOptions.makeCustomAnimation(context, enterAnim, exitAnim)
        activity.startActivity(intent, options.toBundle())
    }

}