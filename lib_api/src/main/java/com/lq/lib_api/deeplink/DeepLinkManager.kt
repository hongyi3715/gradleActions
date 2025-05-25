package com.lq.lib_api.deeplink

import android.app.Application
import android.net.Uri
import androidx.core.net.toUri
import com.lq.lib_annotation.deeplink.IDeepLinkRegister
import com.lq.lib_api.util.LogUtil
import kotlin.collections.forEach

internal object DeepLinkManager {
    private val links = mutableMapOf<String, String>()

    fun init(context: Application){
        val clazz = Class.forName("com.lq.router.DeepLinkIndex")
        val instance = clazz.getField("INSTANCE").get(null) // 拿到 object 的单例实例
        val method = clazz.getDeclaredMethod("getRoots")
        val registers = method.invoke(instance) as List<IDeepLinkRegister>

        registers.forEach {
            it.register(links)
        }
    }

    fun getPathFromUri(uri:String): String?{
        val parsedUri = uri.toUri()
        val schemeHostPath = "${parsedUri.scheme}://${parsedUri.host}${parsedUri.path ?: ""}"
        val path = links[schemeHostPath]
        if(path.isNullOrEmpty()){
            LogUtil.d("DeepLink Manager 找不到路径 path: $path")
        }
       return path
    }

    fun match(uri: Uri){

    }

    fun parseParams(uri: Uri): Map<String, String> {
        val result = mutableMapOf<String, String>()
        uri.queryParameterNames.forEach {
            result[it] = uri.getQueryParameter(it) ?: ""
        }
        return result
    }
}