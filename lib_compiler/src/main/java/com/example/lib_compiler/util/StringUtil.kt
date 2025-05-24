package com.example.lib_compiler.util

import com.google.devtools.ksp.processing.SymbolProcessorEnvironment

fun String.capitalizeFirst(): String{
    if (isEmpty()) return this
    return replaceFirstChar {
        if (it.isLowerCase()) it.titlecase() else it.toString()
    }
}

fun getRootName(name: String) = "HRouterRoot${name.capitalizeFirst()}"

fun getGroupName(name: String) = "HRouterGroup${name.capitalizeFirst()}"


fun SymbolProcessorEnvironment.getModuleName() :String = this.options["moduleName"]?:"default"

fun getModuleNameCapitalize(environment: SymbolProcessorEnvironment) = environment.getModuleName().capitalizeFirst()