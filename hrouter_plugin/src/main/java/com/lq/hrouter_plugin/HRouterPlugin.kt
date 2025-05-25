package com.lq.hrouter_plugin

//import com.android.build.gradle.BaseExtension
import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Author: Lq
 * Date: 2025/4/29
 * Description:  MyPlugin
 */
class HRouterPlugin : Plugin<Project> {
    override fun apply(target: Project) {

        target.plugins.withId("com.android.application") {
            target.afterEvaluate {
             /*   val androidExt = target.extensions.findByType(BaseExtension::class.java)
                val generatedDir = File(target.buildDir, "generated/hrouter_index")
                androidExt?.sourceSets?.getByName("main")?.java?.srcDir(generatedDir)
                androidExt?.sourceSets?.getByName("main")?.kotlin?.srcDir(generatedDir)*/

                val buildRoute = target.tasks.register<ContractTask>("buildRoute", ContractTask::class.java) {
                    group = "buildDebug"
                    description = "构建路由索引"
                }

                // 确保在 KSP 之后运行，且避免循环
                target.tasks.matching { it.name == "kspDebugKotlin" }.configureEach {
                    finalizedBy(buildRoute)
                }
            }
        }


    }
}