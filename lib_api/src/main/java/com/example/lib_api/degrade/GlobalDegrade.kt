package com.example.lib_api.degrade

import android.content.Context
import com.example.lib_annotation.RouteDegrade
import com.example.lib_api.util.showToast

@RouteDegrade
class GlobalDegrade: IRouteDegrade {
    override fun onLost(
        path: String,
        reason: String,
        context: Context
    ): Boolean {
        showToast(context,"页面不存在")
        return true
    }
}