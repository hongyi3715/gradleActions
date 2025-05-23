package build

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class TestBuild : DefaultTask(){

   init {
       group = "TestGradle"
       description = "clean up the locked class.jar"
       dependsOn("kspDebugKotlin")
   }

    @TaskAction
    fun runAction(){
        val rootClassNames = mutableSetOf<String>()
        project.rootProject.subprojects.forEach {
            val metaInfDir = File(it.buildDir, "generated/ksp/debug/resources/META-INF/hrouter_root")
            if(metaInfDir.exists()){
                metaInfDir.listFiles()?.forEach { file ->
                    println("HRouterIndex file:${file.name}")
                    val lines = file.readLines().map { it.trim() }.filter { it.isNotEmpty() }
                    rootClassNames.addAll(lines)
                }
            }
         }


        println("✅ HRouterIndex.kt 生成成功，包含 ${rootClassNames.size} 个 Root 类")
        generateHRouteIndex(rootClassNames)
    }

    private fun generateHRouteIndex(rootClassNames: MutableSet<String>){
        val outputDir = File(project.buildDir, "generated/hrouter_index")
        val outputFile = File(outputDir, "HRouterIndex.kt")

        // 确保父目录存在
        outputFile.parentFile.mkdirs()

        // 构建 HRouterIndex 类内容
        val fileContent = buildString {
            appendLine("package com.lq.router")
            appendLine()
            appendLine("import com.example.lib_annotation.route.IRouteRoot")
            appendLine()
            appendLine("object HRouterIndex {")
            appendLine("    val roots: List<IRouteRoot> by lazy {")
            appendLine("        listOf(")
            rootClassNames.forEach { className ->
                appendLine("            $className(),")
            }
            appendLine("        )")
            appendLine("    }")
            appendLine("}")
        }

        outputFile.writeText(fileContent)

        println("✅ HRouterIndex.kt 已生成，包含 ${rootClassNames.size} 个 Root 类")
        println("路径: ${outputFile.absolutePath}")
    }
}