package com.example.lib_compiler.util

import com.example.lib_annotation.Route
import com.example.lib_annotation.RouteDegrade
import com.squareup.kotlinpoet.ClassName

object Const {

    const val HROUTER_PACKAGE: String = "com.lq.router"

    private const val ROOT_PACKAGE = "com.example.lib_annotation.route"

    private const val META_PACKAGE = "com.example.lib_annotation.data"

    val RouteRootClassName = ClassName(ROOT_PACKAGE,"IRouteRoot")

    val RouteGroupClassName = ClassName(ROOT_PACKAGE,"IRouteGroup")

    val RouteMetaClassName = ClassName(META_PACKAGE,"RouteMeta")

    val InterceptorMetaClassName = ClassName(META_PACKAGE,"InterceptorMeta")

    val MutableListClassName = ClassName("kotlin.collections","MutableList")

    val MutableMapClassName = ClassName("kotlin.collections","MutableMap")

    val StringClassName = ClassName("kotlin","String")

    val RouteQualifiedName = Route::class.qualifiedName!!

    val DegradeQualifiedName = RouteDegrade::class.qualifiedName!!

    const val DEGRADE_SHORT_NAME = "RouteDegrade"

    const val ROUTE_SHORT_NAME = "Route"
}