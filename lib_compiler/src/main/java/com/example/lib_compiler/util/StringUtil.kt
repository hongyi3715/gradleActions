package com.example.lib_compiler.util

 fun String.capitalizeFirst(): String{
    if (isEmpty()) return this
    return replaceFirstChar {
        if (it.isLowerCase()) it.titlecase() else it.toString()
    }
}

fun getRootName(name: String) = "HRouterRoot${name.capitalizeFirst()}"

fun getGroupName(name: String) = "HRouterGroup${name.capitalizeFirst()}"