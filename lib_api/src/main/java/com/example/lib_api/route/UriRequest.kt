package com.example.lib_api.route

data class UriRequest(
    val path:String,
    val group:String,
    val param:Map<String, String>
)