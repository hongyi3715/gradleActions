package com.lq.login

import android.content.Context
import com.lq.lib_annotation.RouteDegrade
import com.lq.lib_api.degrade.IRouteDegrade
import com.lq.lib_api.util.showToast

@RouteDegrade
class LoginDegrade: IRouteDegrade {
    override fun onLost(
        path: String,
        reason: String,
        context: Context
    ): Boolean {
        return if (path.startsWith("/login/")) {
            showToast(context,"跳转登录失败")
            true
        } else false
    }
}