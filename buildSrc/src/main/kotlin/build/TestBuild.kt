package build

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.io.File

abstract class TestBuild : DefaultTask(){

   init {
       group = "TestGradle"
       description = "clean up the locked class.jar"

       doLast {
           println("World")
       }

       doFirst {
           val jarFile = File(project.buildDir, "intermediates/compile_library_classes_jar/debug/bundleLibCompileToJarDebug/classes.jar")
           println("📦 Checking path: ${jarFile.absolutePath}")
           if (jarFile.exists()) {
               println("🔧 Deleting locked classes.jar")
               val deleted = jarFile.delete()
               println("🧹 Delete result: $deleted")
           } else {
               println("⚠️ File not found, skipping deletion")
           }
       }
   }

    @TaskAction
    fun runAction(){
        println("Hello")

    }


}