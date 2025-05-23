package com.example.lib_compiler

import com.example.lib_compiler.interceptor.RouteInterceptorPart
import com.example.lib_compiler.wired.AutoWiredProcessorPart
import com.example.lib_compiler.route.RouteSymbolProcessorPart
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSAnnotated

class HRouterSymbolProcessor(private val environment: SymbolProcessorEnvironment): SymbolProcessor {
    override fun process(resolver: Resolver): List<KSAnnotated> {
        val processors = listOf(
            AutoWiredProcessorPart(environment.logger,environment.codeGenerator),
            RouteSymbolProcessorPart(environment),
            RouteInterceptorPart(environment)
        )
        processors.forEach {
            it.process(resolver)
        }
        return emptyList()
    }
}