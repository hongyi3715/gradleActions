package com.example.lib_api.degrade

import android.content.Context

object DegradeManager {

    private val degrades = mutableListOf<IRouteDegrade>()

    fun addDegrade(degrade: IRouteDegrade){
        degrades.add(degrade)
    }

    fun addDegrades(data: List<IRouteDegrade>){
        degrades.addAll(data)
    }

    fun handleDegrade(path:String,reason:String,context: Context){
        for (degrade in degrades){
            val handled = degrade.onLost(path,reason,context)
            if(handled) return
        }
    }

}