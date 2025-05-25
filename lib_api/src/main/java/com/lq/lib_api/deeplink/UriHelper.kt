package com.lq.lib_api.deeplink

import androidx.core.net.toUri

internal object UriHelper {

    fun parseUri(uri:String){
        val parsedUri = uri.toUri()
        val schemeHostPath = "${parsedUri.scheme}://${parsedUri.host}${parsedUri.path ?: ""}"
    }
}