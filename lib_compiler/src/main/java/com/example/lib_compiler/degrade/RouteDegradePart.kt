package com.example.lib_compiler.degrade

import com.example.lib_compiler.ProcessorPart
import com.example.lib_compiler.util.Const
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeSpec

class RouteDegradePart: ProcessorPart {
    val moduleName = ""
    override fun process(resolver: Resolver) {
        val degrades = mutableListOf<String>()
        resolver.getSymbolsWithAnnotation(Const.DegradeQualifiedName).filterIsInstance<KSClassDeclaration>().forEach {
            val symbol  = it.annotations.first{
                it.shortName.asString() == Const.DEGRADE_SHORT_NAME
            }
            val classPath = it.qualifiedName?.asString() ?:return@forEach
            degrades+=classPath
        }

    }

    fun generateRouteDegradeRegister(){
        val className = "RouteDegrade$moduleName"
        val funSpec = FunSpec.builder("register").addParameter("degrades",Const.MutableListClassName)
        val typeSpec = TypeSpec.objectBuilder(className)
        val fileSpec = FileSpec.builder(Const.HROUTER_PACKAGE,className).addType(typeSpec.build()).build()
    }
}