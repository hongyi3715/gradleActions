package com.example.lib_annotation.register

import com.example.lib_annotation.data.RouteMeta


interface IRouteGroup {

    fun loadInto(routeMap: MutableMap<String, RouteMeta>)
}
