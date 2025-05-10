package com.example.lib_compiler

import com.example.lib_annotation.Route
import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.Resolver
import com.google.devtools.ksp.processing.SymbolProcessor
import com.google.devtools.ksp.symbol.KSAnnotated
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.KModifier
import com.squareup.kotlinpoet.ParameterizedTypeName.Companion.parameterizedBy
import com.squareup.kotlinpoet.PropertySpec
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.asTypeName

class MySymbolProcessor(private val codeGenerator: CodeGenerator): SymbolProcessor {
    private var generated = false
    private val generatedPackageName = "com.lq.router"
    private val interfacePackage = "com.lq.router_base"
    private var hasGenerated = false
    override fun process(resolver: Resolver): List<KSAnnotated> {
        if(hasGenerated)return emptyList()
        val routeMap = mutableMapOf<String, MutableMap<String, String>>()
        resolver.getSymbolsWithAnnotation(Route::class.qualifiedName!!)
            .filterIsInstance<KSClassDeclaration>()
            .forEach {
                val symbol = it.annotations.first {
                    it.shortName.asString() == "Route"
                }
                val path = symbol.arguments.firstOrNull()?.value as? String ?: return@forEach
                val classInfo = it.qualifiedName?.asString() ?: return@forEach
                val group = path.split("/").getOrNull(1) ?: return@forEach

                routeMap.getOrPut(group) { mutableMapOf() }[path] = classInfo
            }
        if(routeMap.isNotEmpty()){
            generateGroup(routeMap)
            generateRoot(routeMap)
            hasGenerated = true
        }
        return emptyList()
    }

    private fun generateRoot(routeMap:Map<String,Map<String, String>>){
        val rootInterface = ClassName("com.example.lib_annotation.register","IRouteRoot")
        val function = FunSpec.builder("loadInto").addModifiers(KModifier.OVERRIDE).addParameter("rootMap",
            ClassName("kotlin.collections","MutableMap").parameterizedBy(ClassName("kotlin","String"),
                ClassName("kotlin","String")))

        for (group in routeMap.keys){
            val className = "com.lq.router.HRouter$\$Group$\$$group"
            function.addStatement("rootMap[%S] = %S",group,className)
        }

        val classInfo = TypeSpec.classBuilder("Root").addSuperinterface(rootInterface).addFunction(function.build()).build()

        val fileSpec = FileSpec.builder(generatedPackageName, "HRouter$\$Root")
            .addType(classInfo).build()

        val file = codeGenerator.createNewFile(Dependencies(false),generatedPackageName,"HRouter$\$Root")
        file.bufferedWriter().use { writer ->
            fileSpec.writeTo(writer)
        }
    }

    fun generateGroup(groupMap: Map<String, Map<String, String>>) {
        for ((groupName,pathMap) in groupMap){
            val className = "HRouter\$\$Group\$\$$groupName"
            val routeMetaClass = ClassName("com.example.lib_annotation.data","RouteMeta")
            val groupInterface = ClassName("com.example.lib_annotation.register", "IRouteGroup")

            val function = FunSpec.builder("loadInto").addModifiers(KModifier.OVERRIDE)
                .addParameter("groupMap",ClassName("kotlin.collections","MutableMap").parameterizedBy(
                    ClassName("kotlin","String"),routeMetaClass))

            for ((path,classInfo) in pathMap){
                val destinationClassName = ClassName.bestGuess(classInfo)
                val routeMetaInstance = CodeBlock.of(
                    "%T(%S, %T::class.java, %S)",
                    routeMetaClass, path, destinationClassName, groupName
                )
                function.addStatement("groupMap[%S] = %L", path, routeMetaInstance)
            }
            val fileSpec = FileSpec.builder(generatedPackageName, className)
                .addType(TypeSpec.classBuilder(className).addSuperinterface(groupInterface).addFunction(function.build()).build()).build()

            val file = codeGenerator.createNewFile(
                Dependencies(aggregating = true),
                generatedPackageName,
                "HRouter\$\$Group\$\$$groupName"
            )
            file.bufferedWriter().use { writer ->
                fileSpec.writeTo(writer)
            }
        }
    }

}