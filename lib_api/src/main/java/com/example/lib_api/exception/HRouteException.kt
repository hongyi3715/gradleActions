package com.example.lib_api.exception


open class HRouteException(message: String,code: Int,cause: Throwable): Exception(message, cause)

