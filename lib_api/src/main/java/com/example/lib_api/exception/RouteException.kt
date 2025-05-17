package com.example.lib_api.exception

open class RouteException(message: String): RuntimeException(message)

class GroupNotFoundException(group: String): RouteException("Group not found :$group")

class PathNotFoundException(path: String): RouteException("Path not found :$path")

class PathIllegalException(path: String): RouteException("Path : $path must start with / and at least 2 segments")


class ActivityNotFoundException(activity: String): RouteException("activity: $activity not found")

class ContextIllegalException(): RouteException("Context is required,Current Context is unknown ")

