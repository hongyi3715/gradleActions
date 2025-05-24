package com.example.lib_api.deeplink

import androidx.core.net.toUri

object UriHelper {

    fun parseUri(uri:String){
        val parsedUri = uri.toUri()
        val schemeHostPath = "${parsedUri.scheme}://${parsedUri.host}${parsedUri.path ?: ""}"
    }
}