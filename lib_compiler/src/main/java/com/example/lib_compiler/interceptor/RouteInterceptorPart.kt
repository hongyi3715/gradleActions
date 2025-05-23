package com.example.lib_compiler.interceptor

import com.example.lib_annotation.RouterInterceptor
import com.example.lib_compiler.ProcessorPart
import com.example.lib_compiler.util.Const
import com.example.lib_compiler.util.Const.HROUTER_PACKAGE
import com.example.lib_compiler.util.MetaInfUtil
import com.example.lib_compiler.util.capitalizeFirst
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import kotlin.io.bufferedWriter

class RouteInterceptorPart(environment: SymbolProcessorEnvironment): ProcessorPart {
    private val logger : KSPLogger = environment.logger
    private val codeGenerator: CodeGenerator = environment.codeGenerator
    private val moduleName = environment.options["moduleName"]?:"default"

    override fun process(resolver: Resolver) {
        val routeInterceptors = mutableListOf<String>()
        resolver.getSymbolsWithAnnotation(RouterInterceptor::class.qualifiedName!!).filterIsInstance<KSClassDeclaration>()
            .forEach {
                val symbol = it.annotations.first{
                    it.shortName.asString() == "RouterInterceptor"
                }

                val classInfo = it.qualifiedName?.asString() ?:return@forEach
                val destination = ClassName.bestGuess(classInfo)
                var priority = 0
                var group = ""
                symbol.arguments.forEach { arg ->
                    when (arg.name?.asString()) {
                        "priority" -> priority = arg.value as Int
                        "group" -> group = arg.value as String
                    }
                }
                val mockGroup = "info"
                logger.warn("Route Interceptor Class Path :$classInfo  Priority:$priority")
                routeInterceptors += "InterceptorMeta(\"$classInfo\",$priority,$mockGroup)"
            }
        if(routeInterceptors.isNotEmpty()){
            generateIntercept(routeInterceptors)
        }
    }


    //todo 通过分组
    fun generateIntercept(interceptors:List<String>){
        val className = "InterceptorRegister${moduleName.capitalizeFirst()}"
        val functionBuilder = FunSpec.builder("register")
            .addParameter("interceptors", Const.MutableListClassName
                .parameterizedBy(Const.InterceptorMetaClassName))
            .addModifiers(KModifier.PUBLIC)
        interceptors.forEach {
            functionBuilder.addStatement("interceptors.add($it)")
        }

        val typeBuilder = TypeSpec.objectBuilder(className)
            .addFunction(functionBuilder.build())
        val fileSpec = FileSpec.builder(HROUTER_PACKAGE,className).addType(typeBuilder.build()).build()
        val file = codeGenerator.createNewFile(Dependencies(false),HROUTER_PACKAGE,className)
        file.bufferedWriter().use { writer ->
            fileSpec.writeTo(writer)
        }
        MetaInfUtil.writeMetaInf(codeGenerator,"${HROUTER_PACKAGE}.$className",moduleName,"META-INF/intercept")
    }



}