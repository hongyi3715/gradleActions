package com.example.lib_compiler.deeplink

import com.example.lib_annotation.RouteDeepLink
import com.example.lib_compiler.ProcessorPart
import com.example.lib_compiler.util.Const
import com.example.lib_compiler.util.MetaInfUtil
import com.example.lib_compiler.util.RouteDeepLinkUtil
import com.example.lib_compiler.util.getGroupName
import com.example.lib_compiler.util.getModuleNameCapitalize
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessorEnvironment
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.TypeSpec
import kotlin.sequences.forEach

class RouteDeepLinkPart (private val environment: SymbolProcessorEnvironment): ProcessorPart {
    private val moduleName = getModuleNameCapitalize(environment)
    private val codeGenerator = environment.codeGenerator
    override fun process(resolver: Resolver) {
        if (resolver.getNewFiles().none()) return
        resolver.getSymbolsWithAnnotation(Const.DeepLinkQualifiedName)
            .filterIsInstance<KSClassDeclaration>()
            .forEach {
                val symbol = it.annotations.first {
                    it.shortName.asString() == Const.DEEPLINK_SHORT_NAME
                }
                val classInfo = it.qualifiedName?.asString() ?: return@forEach
                val links = symbol.arguments.firstOrNull()?.value as? List<*> ?: emptyList<Any?>()
                val linkList = links.filterIsInstance<String>()
                environment.logger.warn("Symbol DeepLink : $classInfo  $links")
                RouteDeepLinkUtil.classToLinks(classInfo,linkList.toTypedArray())
            }

        if(RouteDeepLinkUtil.linkIsNotEmpty()){
            generateLinkTable()
        }

    }

    fun generateLinkTable(){
        val className = "DeepLinkRegister${moduleName}"
        val data = RouteDeepLinkUtil.contractPathToLink()
        val function = FunSpec.builder("register")
            .addParameter("map",Const.MutableMapClassName.parameterizedBy(Const.StringClassName,Const.StringClassName))
            .addModifiers(KModifier.OVERRIDE)
        data.forEach {
            it.value.forEach { link->
                function.addStatement("map[%S] = %S", link, it.key)
            }
        }
        val typeSpec = TypeSpec.objectBuilder(className).addSuperinterface(Const.DeepLinkRegisterClassName).addFunction(function.build())
        val fileSpe = FileSpec.builder(Const.HROUTER_PACKAGE,className).addType(typeSpec.build()).build()
        val file = codeGenerator.createNewFile(
            Dependencies(aggregating = true),
            Const.HROUTER_PACKAGE,
            className
        )
        file.bufferedWriter().use { writer ->
            fileSpe.writeTo(writer)
        }
        MetaInfUtil.writeMetaInf(codeGenerator,"${Const.HROUTER_PACKAGE}.$className",moduleName,Const.DEEPLINK_CONTRACT)
        RouteDeepLinkUtil.clear()
    }

}