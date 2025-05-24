package com.example.lib_compiler.util

import com.google.devtools.ksp.processing.KSPLogger

object RouteDeepLinkUtil {

    private val classMap = mutableMapOf<String, String>()

    private val linkMap = mutableMapOf<String, Array<String>>()

    fun classToPath(classInfo:String,path: String){
        classMap[classInfo] = path
    }

    fun classToLinks(classInfo: String,links: Array<String>){
        if(links.isNotEmpty())
        linkMap[classInfo] = links
    }

    fun getLinkMapAndClassMap(logger: KSPLogger){
        classMap.forEach {
            logger.warn("ClassMap  KEY : ${it.key} VALUE: ${it.value}")
        }
        linkMap.forEach {
            logger.warn("LinKMap  KEY : ${it.key} VALUE: ${it.value}")
        }
    }
    fun linkIsNotEmpty() :Boolean = linkMap.isNotEmpty()

    fun clear(){
        classMap.clear()
        linkMap.clear()
    }

    fun contractPathToLink():Map<String, Array<String>>{
        val data = mutableMapOf<String,Array<String>>()
        linkMap.forEach {
            if(classMap.containsKey(it.key)){
                val path = classMap[it.key]!!
                val links = it.value
                data[path] = links
            }
        }
        return data
    }


}