package com.lq.lib_compiler

import com.lq.lib_compiler.deeplink.RouteDeepLinkPart
import com.lq.lib_compiler.degrade.RouteDegradePart
import com.lq.lib_compiler.interceptor.RouteInterceptorPart
import com.lq.lib_compiler.wired.AutoWiredProcessorPart
import com.lq.lib_compiler.route.RouteSymbolProcessorPart
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated

internal class HRouterSymbolProcessor(private val environment: SymbolProcessorEnvironment): SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val processors = listOf(
            AutoWiredProcessorPart(environment.logger,environment.codeGenerator),
            RouteSymbolProcessorPart(environment),
            RouteInterceptorPart(environment),
            RouteDegradePart(environment),
            RouteDeepLinkPart(environment)
        )
        processors.forEach {
            it.process(resolver)
        }
        return emptyList()
    }
}