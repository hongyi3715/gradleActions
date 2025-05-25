package com.lq.lib_compiler.degrade

import com.lq.lib_compiler.ProcessorPart
import com.lq.lib_compiler.util.Const
import com.lq.lib_compiler.util.Const.HROUTER_PACKAGE
import com.lq.lib_compiler.util.MetaInfUtil
import com.lq.lib_compiler.util.capitalizeFirst
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec

internal class RouteDegradePart(private val environment: SymbolProcessorEnvironment): ProcessorPart {
    val moduleName = environment.options["moduleName"]?:"default"
    val codeGenerator = environment.codeGenerator
    override fun process(resolver: Resolver) {
        val degrades = mutableListOf<String>()
        resolver.getSymbolsWithAnnotation(Const.DegradeQualifiedName).filterIsInstance<KSClassDeclaration>().forEach {
            val symbol  = it.annotations.first{
                it.shortName.asString() == Const.DEGRADE_SHORT_NAME
            }
            val classPath = it.qualifiedName?.asString() ?:return@forEach
            var priority = 0
            var group = ""
            symbol.arguments.forEach { arg ->
                when (arg.name?.asString()) {
                    "priority" -> priority = arg.value as Int
                    "group" -> group = arg.value as String
                }
            }
            degrades+=  "DegradeMeta(\"$classPath\",$priority,\"$group\")"
        }
        if(degrades.isNotEmpty()){
            generateRouteDegradeRegister(degrades)
        }

    }

    fun generateRouteDegradeRegister(degrades:List<String>){
        val className = "RouteDegrade${moduleName.capitalizeFirst()}"
        val funSpec = FunSpec.builder("register").addParameter("degrades",Const.MutableListClassName.parameterizedBy(Const.DegradeMetaClassName))
            .addModifiers(KModifier.OVERRIDE)

        for(degrade  in degrades){
            funSpec.addStatement("degrades.add($degrade)")
        }
        val typeSpec = TypeSpec.objectBuilder(className).addFunction(funSpec.build()).addSuperinterface(Const.DegradeRegisterClassName)
        val fileSpec = FileSpec.builder(Const.HROUTER_PACKAGE,className).addType(typeSpec.build()).build()

        val file = codeGenerator.createNewFile(Dependencies(false),Const.HROUTER_PACKAGE,className)

        file.bufferedWriter().use {
            fileSpec.writeTo(it)
        }
        MetaInfUtil.writeMetaInf(codeGenerator,"${HROUTER_PACKAGE}.$className",moduleName,Const.DEGRADE_CONTRACT)
    }
}