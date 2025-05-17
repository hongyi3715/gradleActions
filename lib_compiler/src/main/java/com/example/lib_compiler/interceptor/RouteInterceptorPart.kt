package com.example.lib_compiler.interceptor

import com.example.lib_annotation.RouterInterceptor
import com.example.lib_compiler.ProcessorPart
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration

class RouteInterceptorPart(private val logger: KSPLogger,private val codeGenerator: CodeGenerator): ProcessorPart {
    override fun process(resolver: Resolver) {
        val symbols = resolver.getSymbolsWithAnnotation(RouterInterceptor::class.java.simpleName).filterIsInstance<KSClassDeclaration>()
            .forEach {
                val symbol = it.annotations.first{
                    it.shortName.asString() == "RouteInterceptor"
                }
                val priority = symbol.arguments.firstOrNull()?.value as Int
            }
    }
}