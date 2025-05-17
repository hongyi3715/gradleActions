package com.example.lib_api.route

import android.util.Log
import com.example.lib_annotation.data.RouteMeta
import com.example.lib_annotation.route.IRouteGroup
import com.example.lib_annotation.route.IRouteRoot
import com.example.lib_api.exception.GroupNotFoundException
import com.example.lib_api.exception.PathIllegalException
import com.example.lib_api.exception.PathNotFoundException
import com.example.lib_api.util.LogUtil
import java.util.concurrent.ConcurrentHashMap

object RouteHelper {
    private val routeRoot = mutableMapOf<String, String>()
    private val groupCache = ConcurrentHashMap<String, Map<String, RouteMeta>>()

    //NOTE 插件化时应判断是否包含当前插件Root
    fun findRoot(){
        if (routeRoot.isEmpty()) {
            val rootClazz = Class.forName("com.lq.router.Root")
            val root = rootClazz.getDeclaredConstructor().newInstance() as IRouteRoot
            root.loadInto(routeRoot)
            routeRoot.forEach {
                LogUtil.d( "root: ${it.key}  and ${it.value}")
            }
        }
    }

    fun findGroup(path: String): RouteMeta {
        val segments = path.split("/")
        require(path.startsWith("/") && segments.size >= 2) { throw PathIllegalException(path) }
        val group = segments[1]
        val groupPath = routeRoot[group] ?: throw GroupNotFoundException(group)
        val groupMap = getGroupFromCache(group)
        val routeMeta = groupMap?.get(path)
       return routeMeta?:getGroup(groupPath,path)
    }

    private fun getGroup(groupPath: String,path: String): RouteMeta {
        val group = Class.forName(groupPath)
        val groupInfo = group.getDeclaredConstructor().newInstance() as IRouteGroup
        val myGroupMap = mutableMapOf<String, RouteMeta>()
        groupInfo.loadInto(myGroupMap)
        val routeMeta = myGroupMap[path] ?: throw PathNotFoundException(path)
        cacheGroup(groupPath,myGroupMap)
        return routeMeta
    }

    private fun getGroupFromCache(group: String): Map<String, RouteMeta>?{
        return groupCache[group]
    }

    private fun cacheGroup(group: String,map:Map<String, RouteMeta>){
        groupCache.put(group, map)
    }
}