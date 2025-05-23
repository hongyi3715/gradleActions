package com.example.lib_annotation.route

import com.example.lib_annotation.data.RouteMeta


interface IRouteGroup {

    fun loadInto(groupMap: MutableMap<String, RouteMeta>)
}
