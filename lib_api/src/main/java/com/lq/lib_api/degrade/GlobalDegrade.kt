package com.lq.lib_api.degrade

import android.content.Context
import com.lq.lib_annotation.RouteDegrade
import com.lq.lib_api.util.showToast

@RouteDegrade
internal class GlobalDegrade: IRouteDegrade {
    override fun onLost(
        path: String,
        reason: String,
        context: Context
    ): Boolean {
        showToast(context,"页面不存在")
        return true
    }
}