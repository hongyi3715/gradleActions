package com.lq.lib_annotation.data

data class InterceptorMeta(
    val destination: String,
    val priority: Int =0,
    val group: String = ""
)