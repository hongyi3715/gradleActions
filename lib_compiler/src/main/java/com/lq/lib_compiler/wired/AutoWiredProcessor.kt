package com.lq.lib_compiler.wired

import com.lq.lib_annotation.AutoWired
import com.lq.lib_compiler.ProcessorPart
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration

internal class AutoWiredProcessorPart(private val logger: KSPLogger,private val codeGenerator: CodeGenerator):
    ProcessorPart {
    override fun process(resolver: Resolver) {
        val symbols = resolver.getSymbolsWithAnnotation(AutoWired::class.qualifiedName!!)
        val classSet = symbols .mapNotNull { it.parent as? KSClassDeclaration }.distinct()
        classSet.forEach {
            it.accept(AutoWiredVisitor(logger, codeGenerator), Unit)
        }
    }
}

