package com.lq.lib_api.degrade

import android.content.Context

interface IRouteDegrade {

    fun onLost(path:String,reason:String,context: Context): Boolean
}