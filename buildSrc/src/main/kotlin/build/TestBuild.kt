package build

import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction

abstract class TestBuild : DefaultTask(){

   init {
       group = "TestGradle"
       description = "test how to use gradle"

       dependsOn("assembleDebug")


       doLast {
           println("World")
       }
   }

    @TaskAction
    fun runAction(){
        println("Hello")

    }


}