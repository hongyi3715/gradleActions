package com.example.lib_api.degrade

import android.content.Context
import com.example.lib_annotation.data.DegradeMeta
import com.example.lib_annotation.degrade.IDegradeRegister

object DegradeManager {

    private val degrades = mutableListOf<IRouteDegrade>()


    fun addDegrade(degrade: IRouteDegrade){
        degrades.add(degrade)
    }

    fun init(context: Context){
        val clazz = Class.forName("com.lq.router.DegradeIndex")
        val instance = clazz.getField("INSTANCE").get(null) // 拿到 object 的单例实例
        val method = clazz.getDeclaredMethod("getRoots")
        val registers = method.invoke(instance) as List<IDegradeRegister>
         val data = mutableListOf<DegradeMeta>()

        registers.forEach {
            it.register(data)
        }
        data.sortedBy { it.priority }.toMutableList().forEach {
            DegradeFactory.create(context,it.path).apply {
                addDegrade(this)
            }
        }
    }

    fun handleDegrade(path:String,reason:String,context: Context){
        for (degrade in degrades){
            val handled = degrade.onLost(path,reason,context)
            if(handled) return
        }
    }

}