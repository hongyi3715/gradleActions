package com.lq.lib_compiler.util

import com.lq.lib_annotation.Route
import com.lq.lib_annotation.RouteDeepLink
import com.lq.lib_annotation.RouteDegrade
import com.lq.lib_annotation.RouterInterceptor
import com.squareup.kotlinpoet.ClassName

internal object Const {

    const val HROUTER_PACKAGE: String = "com.lq.router"

    private const val ROOT_PACKAGE = "com.lq.lib_annotation.route"

    private const val INTERCEPTOR_PACKAGE = "com.lq.lib_annotation.interceptor"

    private const val DEGRADE_PACKAGE = "com.lq.lib_annotation.degrade"

    private const val DEEPLINK_PACKAGE = "com.lq.lib_annotation.deeplink"

    private const val META_PACKAGE = "com.lq.lib_annotation.data"

    val RouteRootClassName = ClassName(ROOT_PACKAGE,"IRouteRoot")

    val RouteGroupClassName = ClassName(ROOT_PACKAGE,"IRouteGroup")

    val RouteMetaClassName = ClassName(META_PACKAGE,"RouteMeta")

    val InterceptorMetaClassName = ClassName(META_PACKAGE,"InterceptorMeta")

    val DegradeMetaClassName = ClassName(META_PACKAGE,"DegradeMeta")

    val InterceptorRegisterClassName = ClassName(INTERCEPTOR_PACKAGE,"IInterceptorRegister")

    val DegradeRegisterClassName = ClassName(DEGRADE_PACKAGE,"IDegradeRegister")

    val DeepLinkRegisterClassName = ClassName(DEEPLINK_PACKAGE,"IDeepLinkRegister")

    val MutableListClassName = ClassName("kotlin.collections","MutableList")

    val MutableMapClassName = ClassName("kotlin.collections","MutableMap")

    val StringClassName = ClassName("kotlin","String")

    val RouteQualifiedName = Route::class.qualifiedName!!

    val DegradeQualifiedName = RouteDegrade::class.qualifiedName!!

    val InterceptorQualifiedName = RouterInterceptor::class.qualifiedName!!

    val DeepLinkQualifiedName = RouteDeepLink::class.qualifiedName!!


    const val DEGRADE_SHORT_NAME = "RouteDegrade"

    const val INTERCEPTOR_SHORT_NAME = "RouterInterceptor"

    const val DEEPLINK_SHORT_NAME = "RouteDeepLink"

    const val ROUTE_SHORT_NAME = "Route"

    const val ROUTE_CONTRACT = "META-INF/route"

    const val INTERCEPTOR_CONTRACT = "META-INF/interceptor"

    const val DEGRADE_CONTRACT = "META-INF/degrade"

    const val DEEPLINK_CONTRACT = "META-INF/deeplink"
}