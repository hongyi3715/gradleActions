package com.lq.lib_api.core

import android.content.Context
import com.lq.lib_api.autowired.ParameterBuilder

class RouterBuilder (private val path: String){
    private val delegate = HRouterDelegate(path)

    fun withContext(ctx: Context) = apply { delegate.withContext(ctx) }
    fun withAnim(enter: Int, exit: Int) = apply { delegate.withAnim(enter, exit) }
    fun withParams(block: ParameterBuilder.() -> Unit) = apply { delegate.withParams(block) }
    fun navigate() = delegate.navigate()
}