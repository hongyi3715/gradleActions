package com.example.lib_compiler.wired

import com.google.devtools.ksp.processing.CodeGenerator
import com.google.devtools.ksp.processing.Dependencies
import com.google.devtools.ksp.processing.KSPLogger
import com.google.devtools.ksp.symbol.KSClassDeclaration
import com.google.devtools.ksp.symbol.KSVisitorVoid
import com.squareup.kotlinpoet.ClassName
import com.squareup.kotlinpoet.CodeBlock
import com.squareup.kotlinpoet.FileSpec
import com.squareup.kotlinpoet.FunSpec
import com.squareup.kotlinpoet.TypeName
import com.squareup.kotlinpoet.TypeSpec
import com.squareup.kotlinpoet.ksp.toTypeName

class AutoWiredVisitor(
    private val logger: KSPLogger,
    private val codeGenerator: CodeGenerator
) : KSVisitorVoid() {

    override fun visitClassDeclaration(classDeclaration: KSClassDeclaration, data: Unit) {
        val className = classDeclaration.simpleName.asString()
        val packageName = classDeclaration.packageName.asString()

        logger.warn("className: $className   packageName: $packageName")

        val fileSpec = FileSpec.Companion.builder(packageName, "${className}AutoWired")
        val classSpec = TypeSpec.Companion.classBuilder("${className}AutoWired")

        val funcSpec = FunSpec.Companion.builder("inject")
            .addParameter("target", ClassName(packageName, className))
            .addParameter("bundle", ClassName("android.os", "Bundle").copy(nullable = true))

        classDeclaration.getAllProperties().forEach { property ->
            val symbol = property.annotations.find { it.shortName.asString() == "AutoWired" } ?: return@forEach
            val required = symbol.arguments.first().value as Boolean
            val injectedName = property.simpleName.asString()

            val type = property.type.resolve()
            val nonNullableType = type.makeNotNullable()
            val typeQualifiedName = nonNullableType.declaration.qualifiedName?.asString()

            logger.warn("inject: $injectedName is type:$type   qualified: $typeQualifiedName")

            val typeInfo = when (typeQualifiedName) {
                "kotlin.String" -> "String" to "\"\""
                "kotlin.Int" -> "Int" to "0"
                "kotlin.Double" -> "Double" to "0.0"
                "kotlin.Long" -> "Long" to "0L"
                "kotlin.Float" -> "Float" to "0f"
                "kotlin.Boolean" -> "Boolean" to "false"
                else -> {
                    if (nonNullableType.declaration is KSClassDeclaration) {
                        val superTypes = (nonNullableType.declaration as KSClassDeclaration).superTypes.mapNotNull {
                            it.resolve().declaration.qualifiedName?.asString()
                        }
                        val isParcelable = "android.os.Parcelable" in superTypes || typeQualifiedName == "android.os.Parcelable"
                        val isSerializable = "java.io.Serializable" in superTypes || typeQualifiedName == "java.io.Serializable"
                        when {
                            isParcelable -> "Parcelable" to null
                            isSerializable -> "Serializable" to null
                            else -> {
                                logger.warn("Unsupported type: $typeQualifiedName for field $injectedName")
                                null
                            }
                        }
                    } else {
                        logger.warn("Unknown declaration for $injectedName")
                        null
                    }
                }
            } ?: return@forEach

            val (method, defaultValue) = typeInfo

            when (method) {
                "Parcelable" -> {
                    val codeBlock = generateCodeBlock(injectedName, nonNullableType.toTypeName(), required)
                    funcSpec.addCode(codeBlock)
                }

                "Serializable" -> {
                    funcSpec.addStatement(
                        "target.%L = bundle?.getSerializable(%S) as? %T",
                        injectedName, injectedName, nonNullableType.toTypeName()
                    )
                }

                else -> {
                    if (required) {
                        funcSpec.addStatement(
                            "target.%L = bundle?.get%L(%S)",
                            injectedName, method, injectedName
                        )
                    } else {
                        funcSpec.addStatement(
                            "target.%L = bundle?.get%L(%S) ?: target.%L",
                            injectedName, method, injectedName, injectedName
                        )
                    }
                }
            }
        }

        classSpec.addFunction(funcSpec.build())
        val info = fileSpec.addType(classSpec.build()).build()
        val file = codeGenerator.createNewFile(
            Dependencies(true),
            packageName,
            "${className}AutoWired"
        )
        file.bufferedWriter().use { info.writeTo(it) }

        logger.warn("AutoWiredVisitor completed for $className")
    }

    private fun generateCodeBlock(name: String, type: TypeName, required: Boolean): CodeBlock {
        val codeBuilder = CodeBlock.Companion.builder()

        codeBuilder.beginControlFlow("if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.TIRAMISU)")
        if (required) {
            codeBuilder.addStatement(
                "target.%L = bundle?.getParcelable(%S, %T::class.java)",
                name, name, type
            )
        } else {
            codeBuilder.addStatement(
                "target.%L = bundle?.getParcelable(%S, %T::class.java) ?: target.%L",
                name, name, type, name
            )
        }

        codeBuilder.nextControlFlow("else")
        if (required) {
            codeBuilder.addStatement(
                "target.%L = bundle?.getParcelable(%S)",
                name, name
            )
        } else {
            codeBuilder.addStatement(
                "target.%L = bundle?.getParcelable(%S) ?: target.%L",
                name, name, name
            )
        }

        codeBuilder.endControlFlow()
        return codeBuilder.build()
    }
}