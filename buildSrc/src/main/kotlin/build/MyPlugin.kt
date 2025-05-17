package build

import org.gradle.api.Plugin
import org.gradle.api.Project

/**
 * Author: Lq
 * Date: 2025/4/29
 * Description:  MyPlugin
 */
class MyPlugin : Plugin<Project> {
    override fun apply(target: Project) {
        target.tasks.register<TestBuild>("cleanUpJar", TestBuild::class.java){
            group = "buildDebug"
            description = "test build gradle task"
        }
    }
}